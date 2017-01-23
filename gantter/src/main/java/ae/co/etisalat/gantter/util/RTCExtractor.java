package ae.co.etisalat.gantter.util;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ILoginHandler2;
import com.ibm.team.repository.client.ILoginInfo2;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.UUID;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkflowUtilities;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.Term.Operator;
import com.ibm.team.workitem.common.expression.VariableAttributeExpression;
import com.ibm.team.workitem.common.expression.variables.StatusVariable;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IEnumeration;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo; 
import org.springframework.util.StringUtils;

public class RTCExtractor {
	private static IProgressMonitor myProgressMonitor = new NullProgressMonitor(); // Create a progress monitor
	private static String userId = "rvarakala"; // Retrieve the userId in a secure way
	private static String password = "technologia&123"; // Retrieve the password in a secure way
	private static String repoUri = "https://rtcserver.etisalat.corp.ae:9443/ccm/";
	private static ITeamRepository repo = null;
	
	public static void main2(String[] args) {
		init();
//		getProjectDefects("119565");
//		System.out.println(getProjectTasks("121260"/*"122573"*/));
		System.out.println(getProjectDefects("111025"/*"122573"*/));
//		System.out.println(getProductionDefects());
		destroy();
	}

	public static void init(){
		TeamPlatform.startup();
		System.out.println("Server started: " + TeamPlatform.isStarted());
		
		try {
			// Create credentials
			repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(repoUri);
			repo.registerLoginHandler(new ILoginHandler2() {
				@Override
				public ILoginInfo2 challenge(ITeamRepository repo) {
					return new UsernameAndPasswordLoginInfo(userId, password);
				}
			});
			
			// Login
			repo.login(myProgressMonitor);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void destroy(){
		// Logout
		repo.logout();
		// Shut Down
		TeamPlatform.shutdown();
	}
	
	public static ArrayList<Defect> getProjectDefects(String proposalId){
		ArrayList<Defect> defects = new ArrayList<>();
		
		try {
			
			/* Do all of my work with myserver here. */
			IWorkItemClient workItemClient = (IWorkItemClient)repo.getClientLibrary(IWorkItemClient.class);

			// Load all 'Project Area'
			IProcessItemService connect = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
			List<IProjectArea> p = connect.findAllProjectAreas(null, myProgressMonitor);
			IAuditableClient auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);
			IQueryClient queryClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);
			
			/*List<IQueryableAttribute> attributes = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAllAttributes(p.get(3), auditableClient, myProgressMonitor);
			for (IQueryableAttribute attribute1 : attributes) {
				System.out.println(attribute1.getDisplayName() + " - " + attribute1.getIdentifier());
			}*/
			
			// Project Filter
			IQueryableAttribute attribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.ProjectRecordId", auditableClient, myProgressMonitor);
			Expression expression = new AttributeExpression(attribute, AttributeOperation.EQUALS, proposalId);
			
			// Assignment Group Filter
			/*IQueryableAttribute agAttribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.AssignmentGroup", auditableClient, myProgressMonitor);
			Expression agExpression = new AttributeExpression(agAttribute, AttributeOperation.CONTAINS, "IT_DEV_CBCM");
*/
			// WorkItem Status Filter
			IQueryableAttribute recAttr1 = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), IWorkItem.STATE_PROPERTY, auditableClient, myProgressMonitor);
			Expression recExpr1 = new VariableAttributeExpression(recAttr1, AttributeOperation.NOT_EQUALS, new StatusVariable(IWorkflowInfo.CLOSED_STATES));
			
			// Combine Filters
			Term term= new Term(Operator.AND); 
			term.add(recExpr1); 
			term.add(expression); 
//			term.add(agExpression); 
			
			// Execute Query
			IQueryResult<IResolvedResult<IWorkItem>> result = queryClient.getResolvedExpressionResults(p.get(3), (Expression)term, IWorkItem.FULL_PROFILE);
			System.out.println(p.get(3).getName() + "-" + proposalId + " - " + result.getTotalSize(myProgressMonitor));

			// Fetch Data
			IAttribute rbtAttribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.RaisedByTeam", myProgressMonitor);
			IAttribute iattribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.Environment", myProgressMonitor);
			IAttribute agattribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.AssignmentGroup", myProgressMonitor);

			int qa = 0, uat = 0, prod = 0; 
			while (result.hasNext(myProgressMonitor)) {
				IResolvedResult<IWorkItem> item = result.next(myProgressMonitor);
				String agbt = "";
				try {
					agbt = item.getItem().getValue(agattribute).toString();
				} catch (Throwable e1) {
				}
				
				String assignmentGroup = getLiteralName(agattribute,agbt.substring(agbt.indexOf(":")+1),workItemClient);
//				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$" + assignmentGroup);
				if (StringUtils.isEmpty(assignmentGroup)) assignmentGroup = "";
					
				if (  "Invalid".equalsIgnoreCase(WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()))
					||"Rejected".equalsIgnoreCase(WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()))
					||"Duplicate".equalsIgnoreCase(WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()))
					|| !item.getItem().getWorkItemType().contains("defect")
//					|| !assignmentGroup.contains("IT_DEV_CBCM")
					) {
					continue;
					
				}
				try {
						String rbt = item.getItem().getValue(rbtAttribute).toString();
						String ibt = item.getItem().getValue(iattribute).toString();
						System.out.println(item.getItem().getId() + " - " + WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2())
								+ " - " + getLiteralName(rbtAttribute,rbt.substring(rbt.indexOf(":")+1),workItemClient)
								);
						
						if ("RC".equalsIgnoreCase(getLiteralName(iattribute,ibt.substring(ibt.indexOf(":")+1),workItemClient))
							||"Application Operations".equalsIgnoreCase(getLiteralName(rbtAttribute,rbt.substring(rbt.indexOf(":")+1),workItemClient))) {
							prod++;
							defects.add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
									, WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()), "PROD"));
						}else if (getLiteralName(rbtAttribute,rbt.substring(rbt.indexOf(":")+1),workItemClient).contains("UAT")) {
							uat++;
							defects.add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
									, WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()), "UAT"));
						}else {
							qa++;
							defects.add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
									, WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()), "QA"));
						}
					} catch (Exception e) {
				}
			}
			System.out.println("QA #: " + qa + ", UAt #: " + uat + ", Prod #: " + prod);
			
			// Return Counts
			return defects;
		} catch (TeamRepositoryException e) {
			/* Handle repository exceptions such as login problems here. */
			e.printStackTrace();
		}
		System.out.println("Server stopped: " + !TeamPlatform.isStarted());
	
		return new ArrayList<Defect>();
	}
	
	
	public static ArrayList<Task> getProjectTasks(String proposalId){
		ArrayList<Task> tasks = new ArrayList<>();
		
		try {
			/* Do all of my work with myserver here. */
			IWorkItemClient workItemClient = (IWorkItemClient)repo.getClientLibrary(IWorkItemClient.class);

			// Load all 'Project Area'
			IProcessItemService connect = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
			List<IProjectArea> p = connect.findAllProjectAreas(null, myProgressMonitor);
			IAuditableClient auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);
			IQueryClient queryClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);
			
			// Project Filter
			IQueryableAttribute attribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.ProjectRecordId", auditableClient, myProgressMonitor);
			Expression expression = new AttributeExpression(attribute, AttributeOperation.EQUALS, proposalId);
			
			// Assignment Group Filter
			IQueryableAttribute agAttribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.ProjectRecordId", auditableClient, myProgressMonitor);
			Expression agExpression = new AttributeExpression(agAttribute, AttributeOperation.CONTAINING, "IT_DEV_CBCM");
			
			// Combine Filters
			Term term= new Term(Operator.AND); 
			term.add(expression); 
			term.add(agExpression); 
			
			// Execute Query
			IQueryResult<IResolvedResult<IWorkItem>> result = queryClient.getResolvedExpressionResults(p.get(3), (Expression)term, IWorkItem.FULL_PROFILE);
			System.out.println(p.get(3).getName() + "-" + proposalId + " - " + result.getTotalSize(myProgressMonitor));

			while (result.hasNext(myProgressMonitor)) {
				IResolvedResult<IWorkItem> item = result.next(myProgressMonitor);
				if (!item.getItem().getWorkItemType().contains("task")) {
					continue;
					
				}
				try {
						System.out.println(item.getItem().getId() + " - " + item.getItem().getOwner().toString());
						
						IItemManager itemManager = repo.itemManager();
						ArrayList<IContributorHandle> owners = new ArrayList<>();
						owners.add(item.getItem().getOwner());
						List<IContributor> resolvers = itemManager.fetchCompleteItems(owners,IItemManager.DEFAULT, myProgressMonitor);
						
						IAttribute hpsmCRNumber  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.HpsmCRNumber", myProgressMonitor);
						IAttribute HpsmCRTaskNumber  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.HpsmCRTaskNumber", myProgressMonitor);
						
						tasks.add(new Task(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'"), resolvers.get(0).getName(),
								item.getItem().hasAttribute(hpsmCRNumber) ? item.getItem().getValue(hpsmCRNumber).toString() : "",
								item.getItem().hasAttribute(HpsmCRTaskNumber) ? item.getItem().getValue(HpsmCRTaskNumber).toString(): "") );
						
					} catch (Exception e) {
						e.printStackTrace();
					}
			}

			/*List<IQueryableAttribute> attributes = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAllAttributes(p.get(3), auditableClient, myProgressMonitor);
			for (IQueryableAttribute attribute1 : attributes) {
				System.out.println(attribute1.getDisplayName() + " - " + attribute1.getIdentifier());
			}*/
			
			
			// Return Counts
			return tasks;
		} catch (TeamRepositoryException e) {
			/* Handle repository exceptions such as login problems here. */
			e.printStackTrace();
		}
		System.out.println("Server stopped: " + !TeamPlatform.isStarted());
	
		return new ArrayList<Task>();
	}
	
	public static String getLiteralName(IAttribute attribute, String literalId, IWorkItemClient workItemClient){
   	 	IEnumeration<ILiteral> enumeration; 
        try { 
           enumeration = (IEnumeration<ILiteral>)workItemClient.resolveEnumeration(attribute, null); 
           List<ILiteral> enumerationLiterals = enumeration.getEnumerationLiterals();
           for (ILiteral literal : enumerationLiterals) { 
              if (literal.getIdentifier2().getStringIdentifier().equalsIgnoreCase(literalId)) { 
            	  return literal.getName();
           	  } 
           } 
           return null; 
        } catch (Exception e) { 
       	 	return null; 
        }       
   }
	
	public static Identifier<? extends ILiteral> getIdentifier(IAttribute attribute, String literalId, IWorkItemClient workItemClient){
   	 	IEnumeration<ILiteral> enumeration; 
        try { 
           enumeration = (IEnumeration<ILiteral>)workItemClient.resolveEnumeration(attribute, null); 
           List<ILiteral> enumerationLiterals = enumeration.getEnumerationLiterals();
           for (ILiteral literal : enumerationLiterals) { 
        	   System.out.println("++++++++++" + literal + "^^^" + literal.getIdentifier2().getStringIdentifier());
        	   
              if (literal.getIdentifier2().getStringIdentifier().contains(literalId)) { 
            	  return literal.getIdentifier2();
           	  } 
           } /*if (true) {
        		   System.exit(0);
			}*/
           return null; 
        } catch (Exception e) { 
       	 	return null; 
        }       
   }
	
	public static HashMap<String,ArrayList<Defect>> getProductionDefects(){
		HashMap<String,ArrayList<Defect>> defectsMap = new HashMap<>();
		
		try {
			
			/* Do all of my work with RTC server here. */
			IWorkItemClient workItemClient = (IWorkItemClient)repo.getClientLibrary(IWorkItemClient.class);

			// Load all 'Project Area'
			IProcessItemService connect = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
			List<IProjectArea> p = connect.findAllProjectAreas(null, myProgressMonitor);
			IAuditableClient auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);
			IQueryClient queryClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);

			// WorkItem Status Filter
			IQueryableAttribute recAttr2 = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), IWorkItem.STATE_PROPERTY, auditableClient, myProgressMonitor);
			Expression recExpr2 = new VariableAttributeExpression(recAttr2, AttributeOperation.NOT_EQUALS, new StatusVariable(IWorkflowInfo.CLOSED_STATES));
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(2016, 6, 25);// Fixed date to fetch CQs after
			System.out.println(calendar);
			IQueryableAttribute recAttr4 = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), IWorkItem.CREATION_DATE_PROPERTY, auditableClient, myProgressMonitor);
			Expression recExpr4 = new AttributeExpression(recAttr4, AttributeOperation.AFTER, new Timestamp(calendar.getTimeInMillis()));
			
			IQueryableAttribute recAttr3 = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), IWorkItem.TYPE_PROPERTY, auditableClient, myProgressMonitor);
			Expression recExpr3 = new AttributeExpression(recAttr3, AttributeOperation.CONTAINING, "defect");
			
			IAttribute rbtAttribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.RaisedByTeam", myProgressMonitor);

			IQueryableAttribute recAttr5 = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.RaisedByTeam", auditableClient, myProgressMonitor);
			Expression recExpr5 = new AttributeExpression(recAttr5, AttributeOperation.EQUALS,getIdentifier(rbtAttribute,"ae.corp.etisalat.workitem.enumeration.RaisedByTeam.literal.l8",workItemClient));
			
			
			// Combine Filters
			Term term= new Term(Operator.AND); 
			term.add(recExpr4); 
			term.add(recExpr2); 
			term.add(recExpr3); 
			term.add(recExpr5); 
			
			// Execute Query
			IQueryResult<IResolvedResult<IWorkItem>> result = queryClient.getResolvedExpressionResults(p.get(3), (Expression)term/*recExpr1*/, IWorkItem.FULL_PROFILE);
			System.out.println(p.get(3).getName() + "-" + result.getTotalSize(myProgressMonitor));

			// Fetch Data
			while (result.hasNext(myProgressMonitor)) {
				IResolvedResult<IWorkItem> item = result.next(myProgressMonitor);
				if (  "Invalid".equalsIgnoreCase(WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()))
					||"Rejected".equalsIgnoreCase(WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()))
					||"Duplicate".equalsIgnoreCase(WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()))
					||"Pending For Details".equalsIgnoreCase(WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()))
					) {
					continue;
					
				}
				try {
					IAttribute agattribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.AssignmentGroup", myProgressMonitor);
					String agbt = "";
					String assignmentGroup = "Unassigned";
					if (item.getItem().hasAttribute(agattribute)) {
						agbt = item.getItem().getValue(agattribute).toString();
						assignmentGroup = getLiteralName(agattribute,agbt.substring(agbt.indexOf(":")+1),workItemClient);
					}
					
					System.out.println(item.getItem().getId()+"$$$$$$$$$$$$$$$$$$$$$$$$$$" + assignmentGroup + " - " + item.getItem().hasAttribute(agattribute));
					
					if (defectsMap.get(assignmentGroup) != null) {
						defectsMap.get(assignmentGroup).add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
								, WorkflowUtilities.findWorkflowInfo(item.getItem()
								, myProgressMonitor).getStateName(item.getItem().getState2()), "PROD"));
					}else {
						ArrayList<Defect> defects = new ArrayList<>();
						defects.add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
								, WorkflowUtilities.findWorkflowInfo(item.getItem()
								, myProgressMonitor).getStateName(item.getItem().getState2()), "PROD"));
						defectsMap.put(assignmentGroup,defects);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return defectsMap;
		} catch (TeamRepositoryException e) {
			/* Handle repository exceptions such as login problems here. */
			e.printStackTrace();
		}
		System.out.println("Server stopped: " + !TeamPlatform.isStarted());
	
		return new HashMap<String,ArrayList<Defect>>();
	}
	
	public static ArrayList<Defect> getDefectById(String defectId){
		ArrayList<Defect> defects = new ArrayList<>();
		
		try {
			
			/* Do all of my work with myserver here. */
			IWorkItemClient workItemClient = (IWorkItemClient)repo.getClientLibrary(IWorkItemClient.class);

			// Load all 'Project Area'
			IProcessItemService connect = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
			List<IProjectArea> p = connect.findAllProjectAreas(null, myProgressMonitor);
			IAuditableClient auditableClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);
			IQueryClient queryClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);
			
			// Project Filter
			IQueryableAttribute attribute = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE).findAttribute(p.get(3), IWorkItem.ID_PROPERTY, auditableClient, myProgressMonitor);
			Expression expression = new AttributeExpression(attribute, AttributeOperation.EQUALS, defectId);
			
			// Combine Filters
			Term term= new Term(Operator.AND); 
			term.add(expression); 
			
			// Execute Query
			IQueryResult<IResolvedResult<IWorkItem>> result = queryClient.getResolvedExpressionResults(p.get(3), (Expression)term, IWorkItem.FULL_PROFILE);
			System.out.println(p.get(3).getName() + "-" + defectId + " - " + result.getTotalSize(myProgressMonitor));

			// Fetch Data
			IAttribute rbtAttribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.RaisedByTeam", myProgressMonitor);
			IAttribute iattribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.Environment", myProgressMonitor);
			IAttribute projIdattribute  = workItemClient.findAttribute(p.get(3), "ae.corp.etisalat.workitem.attribute.ProjectRecordId", myProgressMonitor);
			
			int qa = 0, uat = 0, prod = 0; 
			while (result.hasNext(myProgressMonitor)) {
				IResolvedResult<IWorkItem> item = result.next(myProgressMonitor);
				try {
						if (!item.getItem().hasAttribute(iattribute) || !item.getItem().hasAttribute(rbtAttribute)) {
							if (item.getItem().hasAttribute(projIdattribute)) {
								defects.add(new Defect("" + item.getItem().getId(),
										item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'"),
										WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor)
												.getStateName(item.getItem().getState2()),
										"QA", item.getItem().getValue(projIdattribute).toString()));
							}else {
								defects.add(new Defect("" + item.getItem().getId(),
										item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'"),
										WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor)
												.getStateName(item.getItem().getState2()),
										"QA", ""));
							}
							continue;
						}
						
						String rbt = item.getItem().getValue(rbtAttribute).toString();
						String ibt = item.getItem().getValue(iattribute).toString();
						System.out.println(item.getItem().getId() + " - " + WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2())
								+ " - " + getLiteralName(rbtAttribute,rbt.substring(rbt.indexOf(":")+1),workItemClient)
								);
						
						if ("RC".equalsIgnoreCase(getLiteralName(iattribute,ibt.substring(ibt.indexOf(":")+1),workItemClient))
							||"Application Operations".equalsIgnoreCase(getLiteralName(rbtAttribute,rbt.substring(rbt.indexOf(":")+1),workItemClient))) {
							prod++;
							defects.add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
									, WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()), "PROD",
									item.getItem().getValue(projIdattribute).toString()));
						}else if (getLiteralName(rbtAttribute,rbt.substring(rbt.indexOf(":")+1),workItemClient).contains("UAT")) {
							uat++;
							defects.add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
									, WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()), "UAT",
									item.getItem().getValue(projIdattribute).toString()));
						}else {
							qa++;
							defects.add(new Defect(""+item.getItem().getId(), item.getItem().getHTMLSummary().getPlainText().replaceAll("\"", "'")
									, WorkflowUtilities.findWorkflowInfo(item.getItem(), myProgressMonitor).getStateName(item.getItem().getState2()), "QA",
									item.getItem().getValue(projIdattribute).toString()));
						}
					} catch (Exception e) {
						e.printStackTrace();
				}
			}
			System.out.println("QA #: " + qa + ", UAt #: " + uat + ", Prod #: " + prod);
			
			// Return Counts
			return defects;
		} catch (TeamRepositoryException e) {
			/* Handle repository exceptions such as login problems here. */
			e.printStackTrace();
		}
		System.out.println("Server stopped: " + !TeamPlatform.isStarted());
	
		return new ArrayList<Defect>();
	}
}

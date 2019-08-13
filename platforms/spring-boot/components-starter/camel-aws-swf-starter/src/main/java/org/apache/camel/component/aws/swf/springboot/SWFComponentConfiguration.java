begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.swf.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|swf
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|AmazonSimpleWorkflow
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|ActivitySchedulingOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DataConverter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|WorkflowTypeRegistrationOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|worker
operator|.
name|ActivityTypeExecutionOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|worker
operator|.
name|ActivityTypeRegistrationOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The aws-swf component is used for managing workflows from Amazon Simple  * Workflow.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.aws-swf"
argument_list|)
DECL|class|SWFComponentConfiguration
specifier|public
class|class
name|SWFComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the aws-swf component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The AWS SWF default configuration      */
DECL|field|configuration
specifier|private
name|SWFConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Amazon AWS Access Key.      */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**      * Amazon AWS Secret Key.      */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**      * Amazon AWS Region.      */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|SWFConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( SWFConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SWFConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|SWFConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|SWFConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|swf
operator|.
name|SWFConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Amazon AWS Access Key.          */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**          * Amazon AWS Secret Key.          */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**          * Amazon AWS Region. When using this parameter, the configuration will          * expect the capitalized name of the region (for example AP_EAST_1)          * You'll need to use the name Regions.EU_WEST_1.name()          */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**          * The workflow domain to use.          */
DECL|field|domainName
specifier|private
name|String
name|domainName
decl_stmt|;
comment|/**          * The list name to consume activities from.          */
DECL|field|activityList
specifier|private
name|String
name|activityList
decl_stmt|;
comment|/**          * The list name to consume workflows from.          */
DECL|field|workflowList
specifier|private
name|String
name|workflowList
decl_stmt|;
comment|/**          * The workflow or activity event name to use.          */
DECL|field|eventName
specifier|private
name|String
name|eventName
decl_stmt|;
comment|/**          * The workflow or activity event version to use.          */
DECL|field|version
specifier|private
name|String
name|version
decl_stmt|;
comment|/**          * Activity or workflow          */
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
comment|/**          * To configure the ClientConfiguration using the key/values from the          * Map.          */
DECL|field|clientConfigurationParameters
specifier|private
name|Map
name|clientConfigurationParameters
decl_stmt|;
comment|/**          * To configure the AmazonSimpleWorkflowClient using the key/values from          * the Map.          */
DECL|field|sWClientParameters
specifier|private
name|Map
name|sWClientParameters
decl_stmt|;
comment|/**          * To use the given AmazonSimpleWorkflowClient as client          */
DECL|field|amazonSWClient
specifier|private
name|AmazonSimpleWorkflow
name|amazonSWClient
decl_stmt|;
comment|/**          * To configure the StartWorkflowOptions using the key/values from the          * Map.          */
DECL|field|startWorkflowOptionsParameters
specifier|private
name|Map
name|startWorkflowOptionsParameters
decl_stmt|;
comment|/**          * Workflow operation          */
DECL|field|operation
specifier|private
name|String
name|operation
init|=
literal|"START"
decl_stmt|;
comment|/**          * The name of the signal to send to the workflow.          */
DECL|field|signalName
specifier|private
name|String
name|signalName
decl_stmt|;
comment|/**          * The policy to use on child workflows when terminating a workflow.          */
DECL|field|childPolicy
specifier|private
name|String
name|childPolicy
decl_stmt|;
comment|/**          * The reason for terminating a workflow.          */
DECL|field|terminationReason
specifier|private
name|String
name|terminationReason
decl_stmt|;
comment|/**          * The type of the result when a workflow state is queried.          */
DECL|field|stateResultType
specifier|private
name|String
name|stateResultType
decl_stmt|;
comment|/**          * Details for terminating a workflow.          */
DECL|field|terminationDetails
specifier|private
name|String
name|terminationDetails
decl_stmt|;
comment|/**          * Activity execution options          */
DECL|field|activityTypeExecutionOptions
specifier|private
name|ActivityTypeExecutionOptions
name|activityTypeExecutionOptions
decl_stmt|;
comment|/**          * Activity registration options          */
DECL|field|activityTypeRegistrationOptions
specifier|private
name|ActivityTypeRegistrationOptions
name|activityTypeRegistrationOptions
decl_stmt|;
comment|/**          * An instance of          * com.amazonaws.services.simpleworkflow.flow.DataConverter to use for          * serializing/deserializing the data.          */
DECL|field|dataConverter
specifier|private
name|DataConverter
name|dataConverter
decl_stmt|;
comment|/**          * Workflow registration options          */
DECL|field|workflowTypeRegistrationOptions
specifier|private
name|WorkflowTypeRegistrationOptions
name|workflowTypeRegistrationOptions
decl_stmt|;
comment|/**          * Activity scheduling options          */
DECL|field|activitySchedulingOptions
specifier|private
name|ActivitySchedulingOptions
name|activitySchedulingOptions
decl_stmt|;
comment|/**          * Maximum number of threads in work pool for activity.          */
DECL|field|activityThreadPoolSize
specifier|private
name|Integer
name|activityThreadPoolSize
init|=
literal|100
decl_stmt|;
comment|/**          * Set the execution start to close timeout.          */
DECL|field|executionStartToCloseTimeout
specifier|private
name|String
name|executionStartToCloseTimeout
init|=
literal|"3600"
decl_stmt|;
comment|/**          * Set the task start to close timeout.          */
DECL|field|taskStartToCloseTimeout
specifier|private
name|String
name|taskStartToCloseTimeout
init|=
literal|"600"
decl_stmt|;
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
DECL|method|getDomainName ()
specifier|public
name|String
name|getDomainName
parameter_list|()
block|{
return|return
name|domainName
return|;
block|}
DECL|method|setDomainName (String domainName)
specifier|public
name|void
name|setDomainName
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
name|this
operator|.
name|domainName
operator|=
name|domainName
expr_stmt|;
block|}
DECL|method|getActivityList ()
specifier|public
name|String
name|getActivityList
parameter_list|()
block|{
return|return
name|activityList
return|;
block|}
DECL|method|setActivityList (String activityList)
specifier|public
name|void
name|setActivityList
parameter_list|(
name|String
name|activityList
parameter_list|)
block|{
name|this
operator|.
name|activityList
operator|=
name|activityList
expr_stmt|;
block|}
DECL|method|getWorkflowList ()
specifier|public
name|String
name|getWorkflowList
parameter_list|()
block|{
return|return
name|workflowList
return|;
block|}
DECL|method|setWorkflowList (String workflowList)
specifier|public
name|void
name|setWorkflowList
parameter_list|(
name|String
name|workflowList
parameter_list|)
block|{
name|this
operator|.
name|workflowList
operator|=
name|workflowList
expr_stmt|;
block|}
DECL|method|getEventName ()
specifier|public
name|String
name|getEventName
parameter_list|()
block|{
return|return
name|eventName
return|;
block|}
DECL|method|setEventName (String eventName)
specifier|public
name|void
name|setEventName
parameter_list|(
name|String
name|eventName
parameter_list|)
block|{
name|this
operator|.
name|eventName
operator|=
name|eventName
expr_stmt|;
block|}
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
DECL|method|setVersion (String version)
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getClientConfigurationParameters ()
specifier|public
name|Map
name|getClientConfigurationParameters
parameter_list|()
block|{
return|return
name|clientConfigurationParameters
return|;
block|}
DECL|method|setClientConfigurationParameters ( Map clientConfigurationParameters)
specifier|public
name|void
name|setClientConfigurationParameters
parameter_list|(
name|Map
name|clientConfigurationParameters
parameter_list|)
block|{
name|this
operator|.
name|clientConfigurationParameters
operator|=
name|clientConfigurationParameters
expr_stmt|;
block|}
DECL|method|getSWClientParameters ()
specifier|public
name|Map
name|getSWClientParameters
parameter_list|()
block|{
return|return
name|sWClientParameters
return|;
block|}
DECL|method|setSWClientParameters (Map sWClientParameters)
specifier|public
name|void
name|setSWClientParameters
parameter_list|(
name|Map
name|sWClientParameters
parameter_list|)
block|{
name|this
operator|.
name|sWClientParameters
operator|=
name|sWClientParameters
expr_stmt|;
block|}
DECL|method|getAmazonSWClient ()
specifier|public
name|AmazonSimpleWorkflow
name|getAmazonSWClient
parameter_list|()
block|{
return|return
name|amazonSWClient
return|;
block|}
DECL|method|setAmazonSWClient (AmazonSimpleWorkflow amazonSWClient)
specifier|public
name|void
name|setAmazonSWClient
parameter_list|(
name|AmazonSimpleWorkflow
name|amazonSWClient
parameter_list|)
block|{
name|this
operator|.
name|amazonSWClient
operator|=
name|amazonSWClient
expr_stmt|;
block|}
DECL|method|getStartWorkflowOptionsParameters ()
specifier|public
name|Map
name|getStartWorkflowOptionsParameters
parameter_list|()
block|{
return|return
name|startWorkflowOptionsParameters
return|;
block|}
DECL|method|setStartWorkflowOptionsParameters ( Map startWorkflowOptionsParameters)
specifier|public
name|void
name|setStartWorkflowOptionsParameters
parameter_list|(
name|Map
name|startWorkflowOptionsParameters
parameter_list|)
block|{
name|this
operator|.
name|startWorkflowOptionsParameters
operator|=
name|startWorkflowOptionsParameters
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getSignalName ()
specifier|public
name|String
name|getSignalName
parameter_list|()
block|{
return|return
name|signalName
return|;
block|}
DECL|method|setSignalName (String signalName)
specifier|public
name|void
name|setSignalName
parameter_list|(
name|String
name|signalName
parameter_list|)
block|{
name|this
operator|.
name|signalName
operator|=
name|signalName
expr_stmt|;
block|}
DECL|method|getChildPolicy ()
specifier|public
name|String
name|getChildPolicy
parameter_list|()
block|{
return|return
name|childPolicy
return|;
block|}
DECL|method|setChildPolicy (String childPolicy)
specifier|public
name|void
name|setChildPolicy
parameter_list|(
name|String
name|childPolicy
parameter_list|)
block|{
name|this
operator|.
name|childPolicy
operator|=
name|childPolicy
expr_stmt|;
block|}
DECL|method|getTerminationReason ()
specifier|public
name|String
name|getTerminationReason
parameter_list|()
block|{
return|return
name|terminationReason
return|;
block|}
DECL|method|setTerminationReason (String terminationReason)
specifier|public
name|void
name|setTerminationReason
parameter_list|(
name|String
name|terminationReason
parameter_list|)
block|{
name|this
operator|.
name|terminationReason
operator|=
name|terminationReason
expr_stmt|;
block|}
DECL|method|getStateResultType ()
specifier|public
name|String
name|getStateResultType
parameter_list|()
block|{
return|return
name|stateResultType
return|;
block|}
DECL|method|setStateResultType (String stateResultType)
specifier|public
name|void
name|setStateResultType
parameter_list|(
name|String
name|stateResultType
parameter_list|)
block|{
name|this
operator|.
name|stateResultType
operator|=
name|stateResultType
expr_stmt|;
block|}
DECL|method|getTerminationDetails ()
specifier|public
name|String
name|getTerminationDetails
parameter_list|()
block|{
return|return
name|terminationDetails
return|;
block|}
DECL|method|setTerminationDetails (String terminationDetails)
specifier|public
name|void
name|setTerminationDetails
parameter_list|(
name|String
name|terminationDetails
parameter_list|)
block|{
name|this
operator|.
name|terminationDetails
operator|=
name|terminationDetails
expr_stmt|;
block|}
DECL|method|getActivityTypeExecutionOptions ()
specifier|public
name|ActivityTypeExecutionOptions
name|getActivityTypeExecutionOptions
parameter_list|()
block|{
return|return
name|activityTypeExecutionOptions
return|;
block|}
DECL|method|setActivityTypeExecutionOptions ( ActivityTypeExecutionOptions activityTypeExecutionOptions)
specifier|public
name|void
name|setActivityTypeExecutionOptions
parameter_list|(
name|ActivityTypeExecutionOptions
name|activityTypeExecutionOptions
parameter_list|)
block|{
name|this
operator|.
name|activityTypeExecutionOptions
operator|=
name|activityTypeExecutionOptions
expr_stmt|;
block|}
DECL|method|getActivityTypeRegistrationOptions ()
specifier|public
name|ActivityTypeRegistrationOptions
name|getActivityTypeRegistrationOptions
parameter_list|()
block|{
return|return
name|activityTypeRegistrationOptions
return|;
block|}
DECL|method|setActivityTypeRegistrationOptions ( ActivityTypeRegistrationOptions activityTypeRegistrationOptions)
specifier|public
name|void
name|setActivityTypeRegistrationOptions
parameter_list|(
name|ActivityTypeRegistrationOptions
name|activityTypeRegistrationOptions
parameter_list|)
block|{
name|this
operator|.
name|activityTypeRegistrationOptions
operator|=
name|activityTypeRegistrationOptions
expr_stmt|;
block|}
DECL|method|getDataConverter ()
specifier|public
name|DataConverter
name|getDataConverter
parameter_list|()
block|{
return|return
name|dataConverter
return|;
block|}
DECL|method|setDataConverter (DataConverter dataConverter)
specifier|public
name|void
name|setDataConverter
parameter_list|(
name|DataConverter
name|dataConverter
parameter_list|)
block|{
name|this
operator|.
name|dataConverter
operator|=
name|dataConverter
expr_stmt|;
block|}
DECL|method|getWorkflowTypeRegistrationOptions ()
specifier|public
name|WorkflowTypeRegistrationOptions
name|getWorkflowTypeRegistrationOptions
parameter_list|()
block|{
return|return
name|workflowTypeRegistrationOptions
return|;
block|}
DECL|method|setWorkflowTypeRegistrationOptions ( WorkflowTypeRegistrationOptions workflowTypeRegistrationOptions)
specifier|public
name|void
name|setWorkflowTypeRegistrationOptions
parameter_list|(
name|WorkflowTypeRegistrationOptions
name|workflowTypeRegistrationOptions
parameter_list|)
block|{
name|this
operator|.
name|workflowTypeRegistrationOptions
operator|=
name|workflowTypeRegistrationOptions
expr_stmt|;
block|}
DECL|method|getActivitySchedulingOptions ()
specifier|public
name|ActivitySchedulingOptions
name|getActivitySchedulingOptions
parameter_list|()
block|{
return|return
name|activitySchedulingOptions
return|;
block|}
DECL|method|setActivitySchedulingOptions ( ActivitySchedulingOptions activitySchedulingOptions)
specifier|public
name|void
name|setActivitySchedulingOptions
parameter_list|(
name|ActivitySchedulingOptions
name|activitySchedulingOptions
parameter_list|)
block|{
name|this
operator|.
name|activitySchedulingOptions
operator|=
name|activitySchedulingOptions
expr_stmt|;
block|}
DECL|method|getActivityThreadPoolSize ()
specifier|public
name|Integer
name|getActivityThreadPoolSize
parameter_list|()
block|{
return|return
name|activityThreadPoolSize
return|;
block|}
DECL|method|setActivityThreadPoolSize (Integer activityThreadPoolSize)
specifier|public
name|void
name|setActivityThreadPoolSize
parameter_list|(
name|Integer
name|activityThreadPoolSize
parameter_list|)
block|{
name|this
operator|.
name|activityThreadPoolSize
operator|=
name|activityThreadPoolSize
expr_stmt|;
block|}
DECL|method|getExecutionStartToCloseTimeout ()
specifier|public
name|String
name|getExecutionStartToCloseTimeout
parameter_list|()
block|{
return|return
name|executionStartToCloseTimeout
return|;
block|}
DECL|method|setExecutionStartToCloseTimeout ( String executionStartToCloseTimeout)
specifier|public
name|void
name|setExecutionStartToCloseTimeout
parameter_list|(
name|String
name|executionStartToCloseTimeout
parameter_list|)
block|{
name|this
operator|.
name|executionStartToCloseTimeout
operator|=
name|executionStartToCloseTimeout
expr_stmt|;
block|}
DECL|method|getTaskStartToCloseTimeout ()
specifier|public
name|String
name|getTaskStartToCloseTimeout
parameter_list|()
block|{
return|return
name|taskStartToCloseTimeout
return|;
block|}
DECL|method|setTaskStartToCloseTimeout (String taskStartToCloseTimeout)
specifier|public
name|void
name|setTaskStartToCloseTimeout
parameter_list|(
name|String
name|taskStartToCloseTimeout
parameter_list|)
block|{
name|this
operator|.
name|taskStartToCloseTimeout
operator|=
name|taskStartToCloseTimeout
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


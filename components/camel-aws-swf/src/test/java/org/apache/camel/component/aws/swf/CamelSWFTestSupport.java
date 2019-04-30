begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.swf
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
package|;
end_package

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
name|AmazonSimpleWorkflowClient
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
name|BindToRegistry
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
name|EndpointInject
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
name|ProducerTemplate
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
DECL|class|CamelSWFTestSupport
specifier|public
class|class
name|CamelSWFTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|options
specifier|protected
name|String
name|options
init|=
literal|"accessKey=key"
operator|+
literal|"&secretKey=secret"
operator|+
literal|"&domainName=testDomain"
operator|+
literal|"&activityList=swf-alist"
operator|+
literal|"&workflowList=swf-wlist"
operator|+
literal|"&version=1.0"
operator|+
literal|"&eventName=testEvent"
operator|+
literal|"&amazonSWClient=#amazonSWClient"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|protected
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"amazonSWClient"
argument_list|)
DECL|field|amazonSWClient
specifier|protected
name|AmazonSimpleWorkflowClient
name|amazonSWClient
init|=
name|mock
argument_list|(
name|AmazonSimpleWorkflowClient
operator|.
name|class
argument_list|)
decl_stmt|;
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openshift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openshift
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IApplication
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IDomain
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
name|Endpoint
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
name|Exchange
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
name|Processor
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
name|impl
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_class
DECL|class|OpenShiftConsumer
specifier|public
class|class
name|OpenShiftConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|method|OpenShiftConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|OpenShiftConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|OpenShiftEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|OpenShiftEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|openshiftServer
init|=
name|OpenShiftHelper
operator|.
name|getOpenShiftServer
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|IDomain
name|domain
init|=
name|OpenShiftHelper
operator|.
name|loginAndGetDomain
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|openshiftServer
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
name|List
argument_list|<
name|IApplication
argument_list|>
name|apps
init|=
name|domain
operator|.
name|getApplications
argument_list|()
decl_stmt|;
comment|// TODO grab state
comment|// TODO: option to only emit if state changes
for|for
control|(
name|IApplication
name|app
range|:
name|apps
control|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|app
argument_list|)
decl_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing exchange."
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|apps
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit


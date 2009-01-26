begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|file
operator|.
name|GenericFileComponent
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
name|file
operator|.
name|GenericFileEndpoint
import|;
end_import

begin_comment
comment|/**  * Base class for remote file components. Polling and consuming files from  * (logically) remote locations  *  * @param<T> the type of file that these remote endpoints provide  */
end_comment

begin_class
DECL|class|RemoteFileComponent
specifier|public
specifier|abstract
class|class
name|RemoteFileComponent
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GenericFileComponent
argument_list|<
name|T
argument_list|>
block|{
DECL|method|RemoteFileComponent ()
specifier|public
name|RemoteFileComponent
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|RemoteFileComponent (CamelContext context)
specifier|public
name|RemoteFileComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterPropertiesSet (GenericFileEndpoint<T> endpoint)
specifier|protected
name|void
name|afterPropertiesSet
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit


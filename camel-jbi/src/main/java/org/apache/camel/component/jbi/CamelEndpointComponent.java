begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|components
operator|.
name|util
operator|.
name|ComponentSupport
import|;
end_import

begin_comment
comment|/**  * The Component activated in the JBIContainer  * @version $Revision: 426415 $  */
end_comment

begin_class
DECL|class|CamelEndpointComponent
specifier|public
class|class
name|CamelEndpointComponent
extends|extends
name|ComponentSupport
block|{
DECL|field|jbiEndpoint
specifier|private
name|JbiEndpoint
name|jbiEndpoint
decl_stmt|;
DECL|method|CamelEndpointComponent (JbiEndpoint jbiEndpoint)
name|CamelEndpointComponent
parameter_list|(
name|JbiEndpoint
name|jbiEndpoint
parameter_list|)
block|{
name|this
operator|.
name|jbiEndpoint
operator|=
name|jbiEndpoint
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|jbiEndpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
block|}
end_class

end_unit


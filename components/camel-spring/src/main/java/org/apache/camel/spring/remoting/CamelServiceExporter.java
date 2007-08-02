begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.remoting
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|remoting
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|FactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|remoting
operator|.
name|support
operator|.
name|RemoteExporter
import|;
end_import

begin_comment
comment|/**  * A {@link FactoryBean} to create a proxy to a service exposing a given {@link #getServiceInterface()}  *  * @author chirino  */
end_comment

begin_class
DECL|class|CamelServiceExporter
specifier|public
class|class
name|CamelServiceExporter
extends|extends
name|RemoteExporter
implements|implements
name|FactoryBean
block|{
DECL|field|singleton
specifier|private
name|boolean
name|singleton
init|=
literal|true
decl_stmt|;
DECL|method|getObject ()
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getProxyForService
argument_list|()
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
name|getObjectType
parameter_list|()
block|{
return|return
name|getServiceInterface
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|singleton
return|;
block|}
DECL|method|setSingleton (boolean singleton)
specifier|public
name|void
name|setSingleton
parameter_list|(
name|boolean
name|singleton
parameter_list|)
block|{
name|this
operator|.
name|singleton
operator|=
name|singleton
expr_stmt|;
block|}
block|}
end_class

end_unit


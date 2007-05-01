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
name|component
operator|.
name|pojo
operator|.
name|PojoComponent
import|;
end_import

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
name|UrlBasedRemoteAccessor
import|;
end_import

begin_comment
comment|/**  * Creates a Proxy to Camel Pojo Endpoint.  *    * @author chirino  */
end_comment

begin_class
DECL|class|CamelProxyFactoryBean
specifier|public
class|class
name|CamelProxyFactoryBean
extends|extends
name|UrlBasedRemoteAccessor
implements|implements
name|FactoryBean
block|{
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|serviceProxy
specifier|private
name|Object
name|serviceProxy
decl_stmt|;
annotation|@
name|Override
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
block|{
name|super
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
try|try
block|{
name|this
operator|.
name|serviceProxy
operator|=
name|PojoComponent
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|getServiceInterface
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getObject ()
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|serviceProxy
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
literal|true
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
block|}
end_class

end_unit


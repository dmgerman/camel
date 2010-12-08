begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
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
name|CamelContextAware
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
name|model
operator|.
name|IdentifiedType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AbstractCamelFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractCamelFactoryBean
parameter_list|<
name|T
parameter_list|>
extends|extends
name|IdentifiedType
implements|implements
name|CamelContextAware
block|{
annotation|@
name|XmlAttribute
DECL|field|camelContextId
specifier|private
name|String
name|camelContextId
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|getObject ()
specifier|public
specifier|abstract
name|T
name|getObject
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|getCamelContextWithId (String camelContextId)
specifier|protected
specifier|abstract
name|CamelContext
name|getCamelContextWithId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
function_decl|;
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
operator|&&
name|camelContextId
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|=
name|getCamelContextWithId
argument_list|(
name|camelContextId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A CamelContext or a CamelContextId must be injected!"
argument_list|)
throw|;
block|}
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getCamelContextId ()
specifier|public
name|String
name|getCamelContextId
parameter_list|()
block|{
return|return
name|camelContextId
return|;
block|}
DECL|method|setCamelContextId (String camelContextId)
specifier|public
name|void
name|setCamelContextId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
block|{
name|this
operator|.
name|camelContextId
operator|=
name|camelContextId
expr_stmt|;
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
DECL|method|getObjectType ()
specifier|public
specifier|abstract
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|getObjectType
parameter_list|()
function_decl|;
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ContextResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|JAXBContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|json
operator|.
name|JSONConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|json
operator|.
name|JSONJAXBContext
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
name|Constants
import|;
end_import

begin_comment
comment|/**  * A resolver of the JAXB context primed for the Camel XML languages  * which supports JSON as well as XML encoding  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Provider
DECL|class|JAXBContextResolver
specifier|public
specifier|final
class|class
name|JAXBContextResolver
implements|implements
name|ContextResolver
argument_list|<
name|JAXBContext
argument_list|>
block|{
DECL|field|context
specifier|private
specifier|final
name|JAXBContext
name|context
decl_stmt|;
DECL|field|packages
specifier|private
name|String
name|packages
decl_stmt|;
DECL|method|JAXBContextResolver ()
specifier|public
name|JAXBContextResolver
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO we can't use natural with JAXB 2.1.6 or 2.1 for some reason?
name|JSONConfiguration
operator|.
name|Builder
name|builder
init|=
name|JSONConfiguration
operator|.
name|mapped
argument_list|()
decl_stmt|;
comment|//JSONConfiguration.Builder builder = JSONConfiguration.natural();
name|this
operator|.
name|packages
operator|=
name|Constants
operator|.
name|JAXB_CONTEXT_PACKAGES
operator|+
literal|":org.apache.camel.web.model"
expr_stmt|;
name|this
operator|.
name|context
operator|=
operator|new
name|JSONJAXBContext
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|,
name|packages
argument_list|)
expr_stmt|;
block|}
DECL|method|getContext (Class<?> objectType)
specifier|public
name|JAXBContext
name|getContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectType
parameter_list|)
block|{
name|Package
name|aPackage
init|=
name|objectType
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|aPackage
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|aPackage
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|packages
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|context
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|getContext ()
specifier|public
name|JAXBContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
end_class

end_unit


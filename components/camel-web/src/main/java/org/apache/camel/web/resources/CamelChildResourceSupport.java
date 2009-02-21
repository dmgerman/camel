begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
package|;
end_package

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
name|view
operator|.
name|ImplicitProduces
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
name|ProducerTemplate
import|;
end_import

begin_comment
comment|/**  * A useful base class for any sub resource of the root {@link org.apache.camel.web.resources.CamelContextResource}  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|ImplicitProduces
argument_list|(
name|Constants
operator|.
name|HTML_MIME_TYPES
argument_list|)
DECL|class|CamelChildResourceSupport
specifier|public
class|class
name|CamelChildResourceSupport
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|template
specifier|private
specifier|final
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|contextResource
specifier|private
name|CamelContextResource
name|contextResource
decl_stmt|;
DECL|method|CamelChildResourceSupport (CamelContextResource contextResource)
specifier|public
name|CamelChildResourceSupport
parameter_list|(
name|CamelContextResource
name|contextResource
parameter_list|)
block|{
name|this
operator|.
name|contextResource
operator|=
name|contextResource
expr_stmt|;
name|camelContext
operator|=
name|contextResource
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|template
operator|=
name|contextResource
operator|.
name|getTemplate
argument_list|()
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getTemplate ()
specifier|public
name|ProducerTemplate
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|getContextResource ()
specifier|public
name|CamelContextResource
name|getContextResource
parameter_list|()
block|{
return|return
name|contextResource
return|;
block|}
block|}
end_class

end_unit


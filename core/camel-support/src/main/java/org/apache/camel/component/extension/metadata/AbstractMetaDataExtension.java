begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension.metadata
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
operator|.
name|metadata
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
name|Component
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
name|ComponentAware
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
name|extension
operator|.
name|MetaDataExtension
import|;
end_import

begin_class
DECL|class|AbstractMetaDataExtension
specifier|public
specifier|abstract
class|class
name|AbstractMetaDataExtension
implements|implements
name|MetaDataExtension
implements|,
name|ComponentAware
implements|,
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|component
specifier|private
name|Component
name|component
decl_stmt|;
DECL|method|AbstractMetaDataExtension ()
specifier|protected
name|AbstractMetaDataExtension
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractMetaDataExtension (Component component)
specifier|protected
name|AbstractMetaDataExtension
parameter_list|(
name|Component
name|component
parameter_list|)
block|{
name|this
argument_list|(
name|component
argument_list|,
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractMetaDataExtension (CamelContext camelContext)
specifier|protected
name|AbstractMetaDataExtension
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractMetaDataExtension (Component component, CamelContext camelContext)
specifier|protected
name|AbstractMetaDataExtension
parameter_list|(
name|Component
name|component
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setComponent (Component component)
specifier|public
name|void
name|setComponent
parameter_list|(
name|Component
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|Component
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
block|}
end_class

end_unit


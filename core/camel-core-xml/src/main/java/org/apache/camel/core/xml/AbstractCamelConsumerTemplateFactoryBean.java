begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ConsumerTemplate
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
name|engine
operator|.
name|DefaultConsumerTemplate
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
name|spi
operator|.
name|Metadata
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A factory for creating a new {@link org.apache.camel.ConsumerTemplate}  * instance with a minimum of XML  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AbstractCamelConsumerTemplateFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractCamelConsumerTemplateFactoryBean
extends|extends
name|AbstractCamelFactoryBean
argument_list|<
name|ConsumerTemplate
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|template
specifier|private
name|ConsumerTemplate
name|template
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets a custom maximum cache size to use in the backing cache pools."
argument_list|)
DECL|field|maximumCacheSize
specifier|private
name|Integer
name|maximumCacheSize
decl_stmt|;
annotation|@
name|Override
DECL|method|getObject ()
specifier|public
name|ConsumerTemplate
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|=
operator|new
name|DefaultConsumerTemplate
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// set custom cache size if provided
if|if
condition|(
name|maximumCacheSize
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setMaximumCacheSize
argument_list|(
name|maximumCacheSize
argument_list|)
expr_stmt|;
block|}
comment|// must start it so its ready to use
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|template
argument_list|)
expr_stmt|;
return|return
name|template
return|;
block|}
annotation|@
name|Override
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|ConsumerTemplate
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|ConsumerTemplate
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getMaximumCacheSize ()
specifier|public
name|Integer
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
name|maximumCacheSize
return|;
block|}
DECL|method|setMaximumCacheSize (Integer maximumCacheSize)
specifier|public
name|void
name|setMaximumCacheSize
parameter_list|(
name|Integer
name|maximumCacheSize
parameter_list|)
block|{
name|this
operator|.
name|maximumCacheSize
operator|=
name|maximumCacheSize
expr_stmt|;
block|}
block|}
end_class

end_unit


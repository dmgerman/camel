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
name|ProducerTemplate
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
name|DefaultProducerTemplate
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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A factory for creating a new {@link org.apache.camel.ProducerTemplate}  * instance with a minimum of XML  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AbstractCamelProducerTemplateFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractCamelProducerTemplateFactoryBean
extends|extends
name|AbstractCamelFactoryBean
argument_list|<
name|ProducerTemplate
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the default endpoint URI used by default for sending message exchanges"
argument_list|)
DECL|field|defaultEndpoint
specifier|private
name|String
name|defaultEndpoint
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
DECL|method|getObject ()
specifier|public
name|ProducerTemplate
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultEndpoint
operator|!=
literal|null
condition|)
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|defaultEndpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No endpoint found for URI: "
operator|+
name|defaultEndpoint
argument_list|)
throw|;
block|}
else|else
block|{
name|template
operator|=
operator|new
name|DefaultProducerTemplate
argument_list|(
name|context
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|template
operator|=
operator|new
name|DefaultProducerTemplate
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|DefaultProducerTemplate
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|DefaultProducerTemplate
operator|.
name|class
return|;
block|}
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
DECL|method|getDefaultEndpoint ()
specifier|public
name|String
name|getDefaultEndpoint
parameter_list|()
block|{
return|return
name|defaultEndpoint
return|;
block|}
comment|/**      * Sets the default endpoint URI used by default for sending message exchanges      */
DECL|method|setDefaultEndpoint (String defaultEndpoint)
specifier|public
name|void
name|setDefaultEndpoint
parameter_list|(
name|String
name|defaultEndpoint
parameter_list|)
block|{
name|this
operator|.
name|defaultEndpoint
operator|=
name|defaultEndpoint
expr_stmt|;
block|}
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
comment|/**      * Sets a custom maximum cache size to use in the backing cache pools.      *      * @param maximumCacheSize the custom maximum cache size      */
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


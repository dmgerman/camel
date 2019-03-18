begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.runtimecatalog.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|runtimecatalog
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|runtimecatalog
operator|.
name|RuntimeCamelCatalog
import|;
end_import

begin_comment
comment|/**  * Default {@link RuntimeCamelCatalog}.  */
end_comment

begin_class
DECL|class|DefaultRuntimeCamelCatalog
specifier|public
class|class
name|DefaultRuntimeCamelCatalog
extends|extends
name|AbstractCamelCatalog
implements|implements
name|RuntimeCamelCatalog
block|{
comment|// cache of operation -> result
DECL|field|cache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|caching
specifier|private
name|boolean
name|caching
decl_stmt|;
comment|/**      * Creates the {@link RuntimeCamelCatalog} without caching enabled.      *      * @param camelContext  the camel context      */
DECL|method|DefaultRuntimeCamelCatalog (CamelContext camelContext)
specifier|public
name|DefaultRuntimeCamelCatalog
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the {@link RuntimeCamelCatalog}      *      * @param camelContext  the camel context      * @param caching  whether to use cache      */
DECL|method|DefaultRuntimeCamelCatalog (CamelContext camelContext, boolean caching)
specifier|public
name|DefaultRuntimeCamelCatalog
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|boolean
name|caching
parameter_list|)
block|{
name|this
operator|.
name|caching
operator|=
name|caching
expr_stmt|;
name|setJSonSchemaResolver
argument_list|(
operator|new
name|CamelContextJSonSchemaResolver
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|modelJSonSchema (String name)
specifier|public
name|String
name|modelJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|answer
operator|=
operator|(
name|String
operator|)
name|cache
operator|.
name|get
argument_list|(
literal|"model-"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|getJSonSchemaResolver
argument_list|()
operator|.
name|getModelJSonSchema
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
literal|"model-"
operator|+
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|componentJSonSchema (String name)
specifier|public
name|String
name|componentJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|answer
operator|=
operator|(
name|String
operator|)
name|cache
operator|.
name|get
argument_list|(
literal|"component-"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|getJSonSchemaResolver
argument_list|()
operator|.
name|getComponentJSonSchema
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
literal|"component-"
operator|+
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|dataFormatJSonSchema (String name)
specifier|public
name|String
name|dataFormatJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|answer
operator|=
operator|(
name|String
operator|)
name|cache
operator|.
name|get
argument_list|(
literal|"dataformat-"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|getJSonSchemaResolver
argument_list|()
operator|.
name|getDataFormatJSonSchema
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
literal|"dataformat-"
operator|+
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|languageJSonSchema (String name)
specifier|public
name|String
name|languageJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// if we try to look method then its in the bean.json file
if|if
condition|(
literal|"method"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"bean"
expr_stmt|;
block|}
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|answer
operator|=
operator|(
name|String
operator|)
name|cache
operator|.
name|get
argument_list|(
literal|"language-"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|getJSonSchemaResolver
argument_list|()
operator|.
name|getLanguageJSonSchema
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|caching
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
literal|"language-"
operator|+
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase.mapping
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
operator|.
name|mapping
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
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CellMappingStrategyFactory
specifier|public
class|class
name|CellMappingStrategyFactory
block|{
DECL|field|STRATEGY
specifier|public
specifier|static
specifier|final
name|String
name|STRATEGY
init|=
literal|"CamelMappingStrategy"
decl_stmt|;
DECL|field|STRATEGY_CLASS_NAME
specifier|public
specifier|static
specifier|final
name|String
name|STRATEGY_CLASS_NAME
init|=
literal|"CamelMappingStrategyClassName"
decl_stmt|;
DECL|field|HEADER
specifier|public
specifier|static
specifier|final
name|String
name|HEADER
init|=
literal|"header"
decl_stmt|;
DECL|field|BODY
specifier|public
specifier|static
specifier|final
name|String
name|BODY
init|=
literal|"body"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CellMappingStrategyFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_STRATIGIES
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|CellMappingStrategy
argument_list|>
name|DEFAULT_STRATIGIES
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|CellMappingStrategy
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|CellMappingStrategyFactory ()
specifier|public
name|CellMappingStrategyFactory
parameter_list|()
block|{
name|DEFAULT_STRATIGIES
operator|.
name|put
argument_list|(
name|HEADER
argument_list|,
operator|new
name|HeaderMappingStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|DEFAULT_STRATIGIES
operator|.
name|put
argument_list|(
name|BODY
argument_list|,
operator|new
name|BodyMappingStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getStrategy (Message message)
specifier|public
name|CellMappingStrategy
name|getStrategy
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|CellMappingStrategy
name|strategy
init|=
literal|null
decl_stmt|;
comment|//Check if strategy has been explicitly set.
if|if
condition|(
name|message
operator|.
name|getHeader
argument_list|(
name|STRATEGY
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|=
name|DEFAULT_STRATIGIES
operator|.
name|get
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|STRATEGY
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|==
literal|null
operator|&&
name|message
operator|.
name|getHeader
argument_list|(
name|STRATEGY_CLASS_NAME
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|=
name|loadStrategyfromClassName
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|STRATEGY_CLASS_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
return|return
name|strategy
return|;
block|}
comment|//Fallback to the default strategy.
return|return
name|DEFAULT_STRATIGIES
operator|.
name|get
argument_list|(
name|HEADER
argument_list|)
return|;
block|}
DECL|method|loadStrategyfromClassName (String strategyClassName)
specifier|private
name|CellMappingStrategy
name|loadStrategyfromClassName
parameter_list|(
name|String
name|strategyClassName
parameter_list|)
block|{
name|CellMappingStrategy
name|strategy
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|CellMappingStrategy
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
name|ClassLoader
name|classLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|classLoader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|clazz
operator|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|CellMappingStrategy
argument_list|>
operator|)
name|classLoader
operator|.
name|loadClass
argument_list|(
name|strategyClassName
argument_list|)
expr_stmt|;
name|strategy
operator|=
name|clazz
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to load HBase cell mapping strategy from class {}."
argument_list|,
name|strategyClassName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|strategy
return|;
block|}
block|}
end_class

end_unit


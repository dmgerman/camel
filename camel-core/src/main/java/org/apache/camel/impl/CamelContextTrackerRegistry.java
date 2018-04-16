begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|spi
operator|.
name|CamelContextTracker
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

begin_comment
comment|/**  * A registry for {@link CamelContextTracker}.  */
end_comment

begin_class
DECL|class|CamelContextTrackerRegistry
specifier|public
specifier|final
class|class
name|CamelContextTrackerRegistry
block|{
comment|/**      * The registry singleton      */
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|CamelContextTrackerRegistry
name|INSTANCE
init|=
operator|new
name|CamelContextTrackerRegistry
argument_list|()
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
name|CamelContextTrackerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|trackers
specifier|private
specifier|final
name|Set
argument_list|<
name|CamelContextTracker
argument_list|>
name|trackers
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|CamelContextTrackerRegistry ()
specifier|private
name|CamelContextTrackerRegistry
parameter_list|()
block|{
comment|// hide constructor
block|}
DECL|method|addTracker (CamelContextTracker tracker)
specifier|public
specifier|synchronized
name|void
name|addTracker
parameter_list|(
name|CamelContextTracker
name|tracker
parameter_list|)
block|{
name|trackers
operator|.
name|add
argument_list|(
name|tracker
argument_list|)
expr_stmt|;
block|}
DECL|method|removeTracker (CamelContextTracker tracker)
specifier|public
specifier|synchronized
name|void
name|removeTracker
parameter_list|(
name|CamelContextTracker
name|tracker
parameter_list|)
block|{
name|trackers
operator|.
name|remove
argument_list|(
name|tracker
argument_list|)
expr_stmt|;
block|}
DECL|method|contextCreated (CamelContext camelContext)
specifier|synchronized
name|void
name|contextCreated
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
for|for
control|(
name|CamelContextTracker
name|tracker
range|:
name|trackers
control|)
block|{
try|try
block|{
if|if
condition|(
name|tracker
operator|.
name|accept
argument_list|(
name|camelContext
argument_list|)
condition|)
block|{
name|tracker
operator|.
name|contextCreated
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error calling CamelContext tracker. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


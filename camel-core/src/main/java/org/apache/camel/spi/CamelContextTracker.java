begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|impl
operator|.
name|CamelContextTrackerRegistry
import|;
end_import

begin_comment
comment|/**  * A camel context creation tracker.  */
end_comment

begin_class
DECL|class|CamelContextTracker
specifier|public
class|class
name|CamelContextTracker
block|{
comment|/**      * Called when a context is created.      */
DECL|method|contextCreated (CamelContext camelContext)
specifier|public
name|void
name|contextCreated
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|open ()
specifier|public
specifier|final
name|void
name|open
parameter_list|()
block|{
name|CamelContextTrackerRegistry
operator|.
name|INSTANCE
operator|.
name|addTracker
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|close ()
specifier|public
specifier|final
name|void
name|close
parameter_list|()
block|{
name|CamelContextTrackerRegistry
operator|.
name|INSTANCE
operator|.
name|removeTracker
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


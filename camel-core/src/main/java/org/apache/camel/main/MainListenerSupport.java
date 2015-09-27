begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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

begin_comment
comment|/**  * A useful base class for {@link org.apache.camel.main.MainListener} implementations.  */
end_comment

begin_class
DECL|class|MainListenerSupport
specifier|public
class|class
name|MainListenerSupport
implements|implements
name|MainListener
block|{
DECL|method|beforeStart (MainSupport main)
specifier|public
name|void
name|beforeStart
parameter_list|(
name|MainSupport
name|main
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|configure (CamelContext context)
specifier|public
name|void
name|configure
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|afterStart (MainSupport main)
specifier|public
name|void
name|afterStart
parameter_list|(
name|MainSupport
name|main
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|beforeStop (MainSupport main)
specifier|public
name|void
name|beforeStop
parameter_list|(
name|MainSupport
name|main
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|afterStop (MainSupport main)
specifier|public
name|void
name|afterStop
parameter_list|(
name|MainSupport
name|main
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit


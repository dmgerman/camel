begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|ContextTestSupport
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MinaComponentWithConfigurationTest
specifier|public
class|class
name|MinaComponentWithConfigurationTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testMinaComponentWithConfiguration ()
specifier|public
name|void
name|testMinaComponentWithConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|MinaComponent
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"mina"
argument_list|,
name|MinaComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|MinaConfiguration
name|cfg
init|=
operator|new
name|MinaConfiguration
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setTextline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setConfiguration
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|cfg
argument_list|,
name|comp
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|MinaEndpoint
name|e1
init|=
operator|(
name|MinaEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"mina://tcp://localhost:4455"
argument_list|)
decl_stmt|;
name|MinaEndpoint
name|e2
init|=
operator|(
name|MinaEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"mina://tcp://localhost:5566?sync=false"
argument_list|)
decl_stmt|;
comment|// should not be same
name|assertNotSame
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|e1
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setPort
argument_list|(
literal|5566
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTextline
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTextline
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4455
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5566
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


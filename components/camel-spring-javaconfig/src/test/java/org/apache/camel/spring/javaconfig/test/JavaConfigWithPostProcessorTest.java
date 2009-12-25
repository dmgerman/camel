begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.javaconfig.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|javaconfig
operator|.
name|test
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
literal|"org.apache.camel.spring.javaconfig.test.MyConfig"
argument_list|,
name|loader
operator|=
name|JavaConfigContextLoader
operator|.
name|class
argument_list|)
annotation|@
name|Component
DECL|class|JavaConfigWithPostProcessorTest
specifier|public
class|class
name|JavaConfigWithPostProcessorTest
extends|extends
name|AbstractJUnit4SpringContextTests
implements|implements
name|Cheese
block|{
DECL|field|doCheeseCalled
specifier|private
name|boolean
name|doCheeseCalled
decl_stmt|;
annotation|@
name|Test
DECL|method|testPostProcessorInjectsMe ()
specifier|public
name|void
name|testPostProcessorInjectsMe
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"doCheese() should be called"
argument_list|,
literal|true
argument_list|,
name|doCheeseCalled
argument_list|)
expr_stmt|;
block|}
DECL|method|doCheese ()
specifier|public
name|void
name|doCheese
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"doCheese called!"
argument_list|)
expr_stmt|;
name|doCheeseCalled
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit


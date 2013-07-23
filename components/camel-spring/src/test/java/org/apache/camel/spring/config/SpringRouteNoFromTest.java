begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
name|spring
operator|.
name|SpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SpringRouteNoFromTest
specifier|public
class|class
name|SpringRouteNoFromTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|createApplicationContext
argument_list|()
expr_stmt|;
block|}
DECL|method|testRouteNoFrom ()
specifier|public
name|void
name|testRouteNoFrom
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
name|AbstractXmlApplicationContext
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/config/SpringRouteNoFromTest.xml"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IllegalArgumentException
name|iae
init|=
operator|(
name|IllegalArgumentException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Route myRoute has no inputs: Route(myRoute)[[] -> [To[mock:result]]]"
argument_list|,
name|iae
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit


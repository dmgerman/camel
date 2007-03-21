begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|builder
operator|.
name|RouteBuilder
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 520164 $  */
end_comment

begin_class
DECL|class|SpringXMLExtensionTest
specifier|public
class|class
name|SpringXMLExtensionTest
extends|extends
name|TestCase
block|{
DECL|field|ctx
specifier|private
name|ClassPathXmlApplicationContext
name|ctx
decl_stmt|;
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
name|ctx
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/spring_xml_extension_test.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|testSimpleRoute ()
specifier|public
name|void
name|testSimpleRoute
parameter_list|()
block|{
name|RouteBuilder
name|builder
init|=
operator|(
name|RouteBuilder
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"testSimpleRoute"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


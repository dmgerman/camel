begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ValidationException
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ValidatorSchemaImportTest
specifier|public
class|class
name|ValidatorSchemaImportTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|validEndpoint
specifier|protected
name|MockEndpoint
name|validEndpoint
decl_stmt|;
DECL|field|finallyEndpoint
specifier|protected
name|MockEndpoint
name|finallyEndpoint
decl_stmt|;
DECL|field|invalidEndpoint
specifier|protected
name|MockEndpoint
name|invalidEndpoint
decl_stmt|;
comment|/**      * Test for the valid schema location      * @throws Exception      */
DECL|method|testRelativeParentSchemaImport ()
specifier|public
name|void
name|testRelativeParentSchemaImport
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/relativeparent/child/child.xsd"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:finally"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<childuser xmlns='http://foo.com/bar'><user><id>1</id><username>Test User</username></user></childuser>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test for the invalid schema import location.      *       * @throws Exception      */
DECL|method|testDotSlashSchemaImport ()
specifier|public
name|void
name|testDotSlashSchemaImport
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/dotslash/child.xsd"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:finally"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<childuser xmlns='http://foo.com/bar'><user><id>1</id><username>Test User</username></user></childuser>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test for the invalid schema import location.      *       * @throws Exception      */
DECL|method|testRelativeDoubleSlashSchemaImport ()
specifier|public
name|void
name|testRelativeDoubleSlashSchemaImport
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|to
argument_list|(
literal|"validator:org/apache/camel/component/validator/doubleslash/child.xsd"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:finally"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<childuser xmlns='http://foo.com/bar'><user><id>1</id><username>Test User</username></user></childuser>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|,
name|finallyEndpoint
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|validEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:valid"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:invalid"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|finallyEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:finally"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ValidatorBeanCallTest
specifier|public
class|class
name|ValidatorBeanCallTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|validEndpoint
specifier|protected
name|MockEndpoint
name|validEndpoint
decl_stmt|;
DECL|field|invalidEndpoint
specifier|protected
name|MockEndpoint
name|invalidEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testCallBean ()
specifier|public
name|void
name|testCallBean
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|invalidEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:rootPath"
argument_list|,
literal|"<report xmlns='http://foo.com/report' xmlns:rb='http://foo.com/report-base'><author><rb:name>Knuth</rb:name></author><content><rb:chapter><rb:subject></rb:subject>"
operator|+
literal|"<rb:abstract></rb:abstract><rb:body></rb:body></rb:chapter></content></report>"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|invalidEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
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
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|MyValidatorBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
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
literal|"direct:rootPath"
argument_list|)
operator|.
name|to
argument_list|(
literal|"validator:bean:myBean.loadFile"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyValidatorBean
specifier|public
specifier|static
class|class
name|MyValidatorBean
block|{
DECL|method|loadFile ()
specifier|public
name|InputStream
name|loadFile
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/report.xsd"
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


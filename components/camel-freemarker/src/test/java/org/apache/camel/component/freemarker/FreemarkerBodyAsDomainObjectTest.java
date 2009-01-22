begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.freemarker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|freemarker
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
comment|/**  * Unit test with the body as a Domain object.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FreemarkerBodyAsDomainObjectTest
specifier|public
class|class
name|FreemarkerBodyAsDomainObjectTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testWithObject ()
specifier|public
name|void
name|testWithObject
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hi Claus how are you? Its a nice day.\nGive my regards to the family Ibsen."
argument_list|)
expr_stmt|;
name|MyPerson
name|person
init|=
operator|new
name|MyPerson
argument_list|()
decl_stmt|;
name|person
operator|.
name|setFamilyName
argument_list|(
literal|"Ibsen"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setGivenName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:in"
argument_list|,
name|person
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"freemarker:org/apache/camel/component/freemarker/BodyAsDomainObject.ftl"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyPerson
specifier|public
specifier|static
class|class
name|MyPerson
block|{
DECL|field|givenName
specifier|private
name|String
name|givenName
decl_stmt|;
DECL|field|familyName
specifier|private
name|String
name|familyName
decl_stmt|;
DECL|method|getGivenName ()
specifier|public
name|String
name|getGivenName
parameter_list|()
block|{
return|return
name|givenName
return|;
block|}
DECL|method|setGivenName (String givenName)
specifier|public
name|void
name|setGivenName
parameter_list|(
name|String
name|givenName
parameter_list|)
block|{
name|this
operator|.
name|givenName
operator|=
name|givenName
expr_stmt|;
block|}
DECL|method|getFamilyName ()
specifier|public
name|String
name|getFamilyName
parameter_list|()
block|{
return|return
name|familyName
return|;
block|}
DECL|method|setFamilyName (String familyName)
specifier|public
name|void
name|setFamilyName
parameter_list|(
name|String
name|familyName
parameter_list|)
block|{
name|this
operator|.
name|familyName
operator|=
name|familyName
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MyPerson{"
operator|+
literal|"givenName='"
operator|+
name|givenName
operator|+
literal|'\''
operator|+
literal|", familyName='"
operator|+
name|familyName
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
block|}
end_class

end_unit


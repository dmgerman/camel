begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Handler
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
name|language
operator|.
name|XPath
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_comment
comment|/**  * Tests the XPath annotation 'header' value which when set will cause the XPath  * to be evaluated on the required header, otherwise it will be applied to the  * body  */
end_comment

begin_class
DECL|class|BeanWithXPathInjectionUsingHeaderValueTest
specifier|public
class|class
name|BeanWithXPathInjectionUsingHeaderValueTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myBean
specifier|protected
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
DECL|method|testConstantXPathHeaders ()
specifier|public
name|void
name|testConstantXPathHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"bean:myBean"
argument_list|,
literal|"<response>OK</response>"
argument_list|,
literal|"invoiceDetails"
argument_list|,
literal|"<invoice><person><name>Alan</name><date>26/08/2012</date></person></invoice>"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean response:  "
operator|+
name|myBean
argument_list|,
literal|"OK"
argument_list|,
name|myBean
operator|.
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean userName: "
operator|+
name|myBean
argument_list|,
literal|"Alan"
argument_list|,
name|myBean
operator|.
name|userName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean date:  "
operator|+
name|myBean
argument_list|,
literal|"26/08/2012"
argument_list|,
name|myBean
operator|.
name|date
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|field|userName
specifier|public
name|String
name|userName
decl_stmt|;
DECL|field|date
specifier|public
name|String
name|date
decl_stmt|;
DECL|field|response
specifier|public
name|String
name|response
decl_stmt|;
annotation|@
name|Handler
DECL|method|handler (@PathR) String response, @XPath(headerName = R, value = R) String userName, @XPath(headerName = R, value = R, resultType = String.class) String date)
specifier|public
name|void
name|handler
parameter_list|(
annotation|@
name|XPath
argument_list|(
literal|"//response/text()"
argument_list|)
name|String
name|response
parameter_list|,
annotation|@
name|XPath
argument_list|(
name|headerName
operator|=
literal|"invoiceDetails"
argument_list|,
name|value
operator|=
literal|"//invoice/person/name/text()"
argument_list|)
name|String
name|userName
parameter_list|,
annotation|@
name|XPath
argument_list|(
name|headerName
operator|=
literal|"invoiceDetails"
argument_list|,
name|value
operator|=
literal|"//invoice/person/date"
argument_list|,
name|resultType
operator|=
name|String
operator|.
name|class
argument_list|)
name|String
name|date
parameter_list|)
block|{
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


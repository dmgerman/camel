begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Exchange
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
name|support
operator|.
name|DefaultExchange
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|XQueryStripWhitespaceTest
specifier|public
class|class
name|XQueryStripWhitespaceTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testStripAll ()
specifier|public
name|void
name|testStripAll
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/payload.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|XQueryBuilder
name|xquery
init|=
name|XQueryBuilder
operator|.
name|xquery
argument_list|(
literal|"//payload"
argument_list|)
operator|.
name|asString
argument_list|()
operator|.
name|stripsAllWhiteSpace
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|xquery
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"012010-10-04JohnDoeThis is a test reportserver is downsomeone@somewhere.com12345678"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStripIgnorable ()
specifier|public
name|void
name|testStripIgnorable
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/payload.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|XQueryBuilder
name|xquery
init|=
name|XQueryBuilder
operator|.
name|xquery
argument_list|(
literal|"//payload"
argument_list|)
operator|.
name|asString
argument_list|()
operator|.
name|stripsIgnorableWhiteSpace
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|xquery
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// replace double spaces to make equals easier
name|result
operator|=
name|result
operator|.
name|replaceAll
argument_list|(
literal|"\\s{2,}"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" 01 2010-10-04 John Doe This is a test report server is down someone@somewhere.com 12345678 "
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


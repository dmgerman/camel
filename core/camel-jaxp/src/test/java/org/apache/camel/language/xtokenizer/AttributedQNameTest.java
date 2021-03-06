begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.xtokenizer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|xtokenizer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|xtokenizer
operator|.
name|XMLTokenExpressionIterator
operator|.
name|AttributedQName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|AttributedQNameTest
specifier|public
class|class
name|AttributedQNameTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testMatches ()
specifier|public
name|void
name|testMatches
parameter_list|()
block|{
name|AttributedQName
name|aqname
init|=
operator|new
name|AttributedQName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petra"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:bar"
argument_list|,
literal|"petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petira"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|aqname
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"*tra"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"astra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"sandra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|aqname
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"pe*"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"astra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"peach"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"peteria"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|aqname
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"p*t*a"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"astra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"pesandra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"patricia"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|aqname
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"p?t?a"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"patia"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"patricia"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|aqname
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"de.petra"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"de.petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|aqname
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"de.pe*"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"de.petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"de.peach"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"delpeach"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// matches any namespaces qualified and unqualified
name|aqname
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"*"
argument_list|,
literal|"p*a"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"petra"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:bar"
argument_list|,
literal|"patia"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
literal|"patricia"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|aqname
operator|.
name|matches
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:bar"
argument_list|,
literal|"peach"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


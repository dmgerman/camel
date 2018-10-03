begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|CamelContext
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
name|Message
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
name|support
operator|.
name|DefaultMessage
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
DECL|class|DefaultMessageHeaderTest
specifier|public
class|class
name|DefaultMessageHeaderTest
extends|extends
name|Assert
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testLookupCaseAgnostic ()
specifier|public
name|void
name|testLookupCaseAgnostic
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupCaseAgnosticAddHeader ()
specifier|public
name|void
name|testLookupCaseAgnosticAddHeader
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupCaseAgnosticAddHeader2 ()
specifier|public
name|void
name|testLookupCaseAgnosticAddHeader2
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupCaseAgnosticAddHeaderRemoveHeader ()
specifier|public
name|void
name|testLookupCaseAgnosticAddHeaderRemoveHeader
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeader
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetWithDifferentCase ()
specifier|public
name|void
name|testSetWithDifferentCase
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"Foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveWithDifferentCase ()
specifier|public
name|void
name|testRemoveWithDifferentCase
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"Foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeader
argument_list|(
literal|"FOO"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeaderWithNullValue ()
specifier|public
name|void
name|testRemoveHeaderWithNullValue
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeader
argument_list|(
literal|"tick"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersWithWildcard ()
specifier|public
name|void
name|testRemoveHeadersWithWildcard
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tack"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tock"
argument_list|,
literal|"blaaa"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bla"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tick"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tock"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeaders
argument_list|(
literal|"t*"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersAllWithWildcard ()
specifier|public
name|void
name|testRemoveHeadersAllWithWildcard
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tack"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tock"
argument_list|,
literal|"blaaa"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bla"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tick"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tock"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeaders
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersWithExclude ()
specifier|public
name|void
name|testRemoveHeadersWithExclude
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tiack"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tiock"
argument_list|,
literal|"blaaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tiuck"
argument_list|,
literal|"blaaaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeaders
argument_list|(
literal|"ti*"
argument_list|,
literal|"tiuck"
argument_list|,
literal|"tiack"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tiack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaaaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tiuck"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersAllWithExclude ()
specifier|public
name|void
name|testRemoveHeadersAllWithExclude
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tack"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tock"
argument_list|,
literal|"blaaa"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bla"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tick"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tock"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeaders
argument_list|(
literal|"*"
argument_list|,
literal|"tick"
argument_list|,
literal|"tock"
argument_list|,
literal|"toe"
argument_list|)
expr_stmt|;
comment|// new message headers
name|assertEquals
argument_list|(
literal|"bla"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tick"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tock"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersWithWildcardInExclude ()
specifier|public
name|void
name|testRemoveHeadersWithWildcardInExclude
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tack"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"taick"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tock"
argument_list|,
literal|"blaaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeaders
argument_list|(
literal|"*"
argument_list|,
literal|"ta*"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"taick"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersWithNulls ()
specifier|public
name|void
name|testRemoveHeadersWithNulls
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tack"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tock"
argument_list|,
literal|"blaaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"taack"
argument_list|,
literal|"blaaaa"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bla"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tick"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tack"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"tock"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"blaaaa"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"taack"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeaders
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersWithNonExcludeHeaders ()
specifier|public
name|void
name|testRemoveHeadersWithNonExcludeHeaders
parameter_list|()
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tick"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tack"
argument_list|,
literal|"blaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"tock"
argument_list|,
literal|"blaaa"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|removeHeaders
argument_list|(
literal|"*"
argument_list|,
literal|"camels"
argument_list|,
literal|"are"
argument_list|,
literal|"fun"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithDefaults ()
specifier|public
name|void
name|testWithDefaults
parameter_list|()
block|{
name|DefaultMessage
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
comment|// must have exchange so to leverage the type converters
name|msg
operator|.
name|setExchange
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"beer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"beer"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|123
argument_list|)
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
literal|"beer"
argument_list|,
literal|"123"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


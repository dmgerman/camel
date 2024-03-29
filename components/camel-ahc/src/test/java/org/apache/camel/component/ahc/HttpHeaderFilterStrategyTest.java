begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
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
name|impl
operator|.
name|DefaultCamelContext
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
name|junit5
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
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|HttpHeaderFilterStrategyTest
specifier|public
class|class
name|HttpHeaderFilterStrategyTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|filter
specifier|private
name|HttpHeaderFilterStrategy
name|filter
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Override
annotation|@
name|BeforeEach
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|filter
operator|=
operator|new
name|HttpHeaderFilterStrategy
argument_list|()
expr_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|applyFilterToExternalHeaders ()
specifier|public
name|void
name|applyFilterToExternalHeaders
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"content-length"
argument_list|,
literal|10
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Content-Length"
argument_list|,
literal|10
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"content-type"
argument_list|,
literal|"text/xml"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/xml"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"cache-control"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Cache-Control"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"connection"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Connection"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"date"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Data"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"pragma"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"trailer"
argument_list|,
literal|"Max-Forwards"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Trailer"
argument_list|,
literal|"Max-Forwards"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"transfer-encoding"
argument_list|,
literal|"chunked"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Transfer-Encoding"
argument_list|,
literal|"chunked"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"upgrade"
argument_list|,
literal|"HTTP/2.0"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Upgrade"
argument_list|,
literal|"HTTP/2.0"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"via"
argument_list|,
literal|"1.1 nowhere.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Via"
argument_list|,
literal|"1.1 nowhere.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"warning"
argument_list|,
literal|"199 Miscellaneous warning"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Warning"
argument_list|,
literal|"199 Miscellaneous warning"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// any Camel header should be filtered
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"CamelHeader"
argument_list|,
literal|"test"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"org.apache.camel.header"
argument_list|,
literal|"test"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"notFilteredHeader"
argument_list|,
literal|"test"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"host"
argument_list|,
literal|"dummy.host.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"Host"
argument_list|,
literal|"dummy.host.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|applyFilterToCamelHeaders ()
specifier|public
name|void
name|applyFilterToCamelHeaders
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"content-length"
argument_list|,
literal|10
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Content-Length"
argument_list|,
literal|10
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"content-type"
argument_list|,
literal|"text/xml"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/xml"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"cache-control"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Cache-Control"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"connection"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Connection"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"date"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Date"
argument_list|,
literal|"close"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"pragma"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"trailer"
argument_list|,
literal|"Max-Forwards"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Trailer"
argument_list|,
literal|"Max-Forwards"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"transfer-encoding"
argument_list|,
literal|"chunked"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Transfer-Encoding"
argument_list|,
literal|"chunked"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"upgrade"
argument_list|,
literal|"HTTP/2.0"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Upgrade"
argument_list|,
literal|"HTTP/2.0"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"via"
argument_list|,
literal|"1.1 nowhere.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Via"
argument_list|,
literal|"1.1 nowhere.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"warning"
argument_list|,
literal|"199 Miscellaneous warning"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Warning"
argument_list|,
literal|"199 Miscellaneous warning"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// any Camel header should be filtered
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"CamelHeader"
argument_list|,
literal|"test"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"org.apache.camel.header"
argument_list|,
literal|"test"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"notFilteredHeader"
argument_list|,
literal|"test"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"host"
argument_list|,
literal|"dummy.host.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Host"
argument_list|,
literal|"dummy.host.com"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


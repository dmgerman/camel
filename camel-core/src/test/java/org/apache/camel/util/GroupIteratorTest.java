begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
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
name|TestSupport
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|GroupIteratorTest
specifier|public
class|class
name|GroupIteratorTest
extends|extends
name|TestSupport
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Override
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
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testGroupIterator ()
specifier|public
name|void
name|testGroupIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|s
init|=
literal|"ABC\nDEF\nGHI\nJKL\nMNO\nPQR\nSTU\nVW"
decl_stmt|;
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|scanner
operator|.
name|useDelimiter
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|GroupIterator
name|gi
init|=
operator|new
name|GroupIterator
argument_list|(
name|exchange
argument_list|,
name|scanner
argument_list|,
literal|"\n"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|gi
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABC\nDEF\nGHI"
argument_list|,
name|gi
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"JKL\nMNO\nPQR"
argument_list|,
name|gi
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"STU\nVW"
argument_list|,
name|gi
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|gi
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|gi
argument_list|)
expr_stmt|;
block|}
DECL|method|testGroupIteratorWithDifferentEncodingFromDefault ()
specifier|public
name|void
name|testGroupIteratorWithDifferentEncodingFromDefault
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|==
name|StandardCharsets
operator|.
name|UTF_8
condition|)
block|{
comment|// can't think of test case where having default charset set to UTF-8 is affected
return|return;
block|}
name|byte
index|[]
name|buf
init|=
literal|"ÃÂ£1\nÃÂ£2\n"
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|in
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|displayName
argument_list|()
argument_list|)
decl_stmt|;
name|scanner
operator|.
name|useDelimiter
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|displayName
argument_list|()
argument_list|)
expr_stmt|;
name|GroupIterator
name|gi
init|=
operator|new
name|GroupIterator
argument_list|(
name|exchange
argument_list|,
name|scanner
argument_list|,
literal|"\n"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|gi
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ÃÂ£1"
argument_list|,
name|gi
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ÃÂ£2"
argument_list|,
name|gi
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|gi
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|gi
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|io
operator|.
name|Closeable
import|;
end_import

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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
DECL|class|XMLTokenExpressionIteratorGroupingTest
specifier|public
class|class
name|XMLTokenExpressionIteratorGroupingTest
extends|extends
name|Assert
block|{
comment|// the input containing multiple Cs
DECL|field|TEST_BODY
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TEST_BODY
init|=
operator|(
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"<c:C attr='4'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='5'>mango</c:C>"
operator|+
literal|"<c:C attr='6'/>"
operator|+
literal|"<c:C attr='7'>pear</c:C>"
operator|+
literal|"<c:C attr='8'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
comment|// one extracted C in its wrapped context per token
DECL|field|RESULTS_WRAPPED_SIZE1
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_WRAPPED_SIZE1
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='4'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='5'>mango</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='6'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='7'>pear</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='8'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|}
decl_stmt|;
comment|// two extracted Cs in their wrapped context per token
DECL|field|RESULTS_WRAPPED_SIZE2
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_WRAPPED_SIZE2
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"<c:C attr='4'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='5'>mango</c:C>"
operator|+
literal|"<c:C attr='6'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='7'>pear</c:C>"
operator|+
literal|"<c:C attr='8'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|}
decl_stmt|;
comment|// at most three extracted Cs in their common wrapped context per token
DECL|field|RESULTS_WRAPPED_SIZE3L
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_WRAPPED_SIZE3L
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='4'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='5'>mango</c:C>"
operator|+
literal|"<c:C attr='6'/>"
operator|+
literal|"<c:C attr='7'>pear</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='8'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|}
decl_stmt|;
comment|// three extracted Cs in their corresponding wrapped contexts per token
DECL|field|RESULTS_WRAPPED_SIZE3U
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_WRAPPED_SIZE3U
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='4'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='5'>mango</c:C>"
operator|+
literal|"<c:C attr='6'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='7'>pear</c:C>"
operator|+
literal|"<c:C attr='8'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|}
decl_stmt|;
comment|// four extracted Cs in their wrapped context per token
DECL|field|RESULTS_WRAPPED_SIZE4
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_WRAPPED_SIZE4
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"<c:C attr='4'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='5'>mango</c:C>"
operator|+
literal|"<c:C attr='6'/>"
operator|+
literal|"<c:C attr='7'>pear</c:C>"
operator|+
literal|"<c:C attr='8'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|}
decl_stmt|;
comment|// at most five extracted Cs in their common wrapped context per token
DECL|field|RESULTS_WRAPPED_SIZE5L
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_WRAPPED_SIZE5L
init|=
name|RESULTS_WRAPPED_SIZE4
decl_stmt|;
comment|// five extracted Cs in their corresponding wrapped contexts per token
DECL|field|RESULTS_WRAPPED_SIZE5U
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_WRAPPED_SIZE5U
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"<c:C attr='4'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='5'>mango</c:C>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:A xmlns:g='urn:g'>"
operator|+
literal|"<c:B attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='6'/>"
operator|+
literal|"<c:C attr='7'>pear</c:C>"
operator|+
literal|"<c:C attr='8'/>"
operator|+
literal|"</c:B>"
operator|+
literal|"</g:A>"
block|}
decl_stmt|;
DECL|field|RESULTS_INJECTED_SIZE1
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_INJECTED_SIZE1
init|=
block|{
literal|"<c:C attr='1' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">peach</c:C>"
block|,
literal|"<c:C attr='2' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|,
literal|"<c:C attr='3' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">orange</c:C>"
block|,
literal|"<c:C attr='4' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|,
literal|"<c:C attr='5' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">mango</c:C>"
block|,
literal|"<c:C attr='6' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|,
literal|"<c:C attr='7' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">pear</c:C>"
block|,
literal|"<c:C attr='8' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|}
decl_stmt|;
DECL|field|RESULTS_INJECTED_SIZE2
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_INJECTED_SIZE2
init|=
block|{
literal|"<group>"
operator|+
literal|"<c:C attr='1' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">peach</c:C>"
operator|+
literal|"<c:C attr='2' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|,
literal|"<group>"
operator|+
literal|"<c:C attr='3' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">orange</c:C>"
operator|+
literal|"<c:C attr='4' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|,
literal|"<group>"
operator|+
literal|"<c:C attr='5' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">mango</c:C>"
operator|+
literal|"<c:C attr='6' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|,
literal|"<group>"
operator|+
literal|"<c:C attr='7' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">pear</c:C>"
operator|+
literal|"<c:C attr='8' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|}
decl_stmt|;
DECL|field|RESULTS_INJECTED_SIZE3
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_INJECTED_SIZE3
init|=
block|{
literal|"<group>"
operator|+
literal|"<c:C attr='1' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">peach</c:C>"
operator|+
literal|"<c:C attr='2' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"<c:C attr='3' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">orange</c:C>"
operator|+
literal|"</group>"
block|,
literal|"<group>"
operator|+
literal|"<c:C attr='4' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"<c:C attr='5' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">mango</c:C>"
operator|+
literal|"<c:C attr='6' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|,
literal|"<group>"
operator|+
literal|"<c:C attr='7' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">pear</c:C>"
operator|+
literal|"<c:C attr='8' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|}
decl_stmt|;
DECL|field|RESULTS_INJECTED_SIZE4
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_INJECTED_SIZE4
init|=
block|{
literal|"<group>"
operator|+
literal|"<c:C attr='1' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">peach</c:C>"
operator|+
literal|"<c:C attr='2' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"<c:C attr='3' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">orange</c:C>"
operator|+
literal|"<c:C attr='4' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|,
literal|"<group>"
operator|+
literal|"<c:C attr='5' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">mango</c:C>"
operator|+
literal|"<c:C attr='6' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"<c:C attr='7' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">pear</c:C>"
operator|+
literal|"<c:C attr='8' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|}
decl_stmt|;
DECL|field|RESULTS_INJECTED_SIZE5
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_INJECTED_SIZE5
init|=
block|{
literal|"<group>"
operator|+
literal|"<c:C attr='1' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">peach</c:C>"
operator|+
literal|"<c:C attr='2' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"<c:C attr='3' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">orange</c:C>"
operator|+
literal|"<c:C attr='4' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"<c:C attr='5' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">mango</c:C>"
operator|+
literal|"</group>"
block|,
literal|"<group>"
operator|+
literal|"<c:C attr='6' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"<c:C attr='7' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\">pear</c:C>"
operator|+
literal|"<c:C attr='8' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
operator|+
literal|"</group>"
block|}
decl_stmt|;
DECL|field|nsmap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsmap
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|nsmap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|nsmap
operator|.
name|put
argument_list|(
literal|"g"
argument_list|,
literal|"urn:g"
argument_list|)
expr_stmt|;
name|nsmap
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|"urn:c"
argument_list|)
expr_stmt|;
block|}
comment|// wrapped mode
annotation|@
name|Test
DECL|method|testExtractWrappedSize1 ()
specifier|public
name|void
name|testExtractWrappedSize1
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'w'
argument_list|,
literal|1
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_WRAPPED_SIZE1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractWrappedSize2 ()
specifier|public
name|void
name|testExtractWrappedSize2
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'w'
argument_list|,
literal|2
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_WRAPPED_SIZE2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractWrappedSize3L ()
specifier|public
name|void
name|testExtractWrappedSize3L
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'w'
argument_list|,
literal|3
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_WRAPPED_SIZE3L
argument_list|)
expr_stmt|;
block|}
comment|// disabled: not working for now as the context extraction across two ancestor paths is not working
DECL|method|disabledtestExtractWrappedSize3U ()
specifier|public
name|void
name|disabledtestExtractWrappedSize3U
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'W'
argument_list|,
literal|3
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_WRAPPED_SIZE3U
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractWrappedSize4 ()
specifier|public
name|void
name|testExtractWrappedSize4
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'w'
argument_list|,
literal|4
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_WRAPPED_SIZE4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractWrappedSize5L ()
specifier|public
name|void
name|testExtractWrappedSize5L
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'w'
argument_list|,
literal|5
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_WRAPPED_SIZE5L
argument_list|)
expr_stmt|;
block|}
comment|// disabled: not working for now as the context extraction across two ancestor paths is not working
DECL|method|disabledtestExtractWrappedSize5U ()
specifier|public
name|void
name|disabledtestExtractWrappedSize5U
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'W'
argument_list|,
literal|5
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_WRAPPED_SIZE5U
argument_list|)
expr_stmt|;
block|}
comment|// injected mode
annotation|@
name|Test
DECL|method|testExtractInjectedSize1 ()
specifier|public
name|void
name|testExtractInjectedSize1
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'i'
argument_list|,
literal|1
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_INJECTED_SIZE1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractInjectedSize2 ()
specifier|public
name|void
name|testExtractInjectedSize2
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'i'
argument_list|,
literal|2
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_INJECTED_SIZE2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractInjectedSize3 ()
specifier|public
name|void
name|testExtractInjectedSize3
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'i'
argument_list|,
literal|3
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_INJECTED_SIZE3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractInjectedSize4 ()
specifier|public
name|void
name|testExtractInjectedSize4
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'i'
argument_list|,
literal|4
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_INJECTED_SIZE4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractInjectedSize5 ()
specifier|public
name|void
name|testExtractInjectedSize5
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'i'
argument_list|,
literal|5
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|RESULTS_INJECTED_SIZE5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractWrappedLeftOver ()
specifier|public
name|void
name|testExtractWrappedLeftOver
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|byte
index|[]
name|data
init|=
operator|(
literal|"<?xml version='1.0' encoding='UTF-8'?><g:A xmlns:g='urn:g'><c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C>"
operator|+
literal|"<c:C attr='2'/>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"</c:B></g:A>"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|final
name|String
index|[]
name|results
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?><g:A xmlns:g='urn:g'><c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='1'>peach</c:C><c:C attr='2'/>"
operator|+
literal|"</c:B></g:A>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:A xmlns:g='urn:g'><c:B attr='1' xmlns:c='urn:c'>"
operator|+
literal|"<c:C attr='3'>orange</c:C>"
operator|+
literal|"</c:B></g:A>"
block|}
decl_stmt|;
name|invokeAndVerify
argument_list|(
literal|"//c:C"
argument_list|,
literal|'w'
argument_list|,
literal|2
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
DECL|method|invokeAndVerify (String path, char mode, int group, InputStream in, String charset, String[] expected)
specifier|private
name|void
name|invokeAndVerify
parameter_list|(
name|String
name|path
parameter_list|,
name|char
name|mode
parameter_list|,
name|int
name|group
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|String
name|charset
parameter_list|,
name|String
index|[]
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|XMLTokenExpressionIterator
name|xtei
init|=
operator|new
name|XMLTokenExpressionIterator
argument_list|(
name|path
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|xtei
operator|.
name|setNamespaces
argument_list|(
name|nsmap
argument_list|)
expr_stmt|;
name|xtei
operator|.
name|setGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|xtei
operator|.
name|createIterator
argument_list|(
name|in
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|Closeable
operator|)
name|it
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"token count"
argument_list|,
name|expected
operator|.
name|length
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"mismatch ["
operator|+
name|i
operator|+
literal|"]"
argument_list|,
name|expected
index|[
name|i
index|]
argument_list|,
name|results
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


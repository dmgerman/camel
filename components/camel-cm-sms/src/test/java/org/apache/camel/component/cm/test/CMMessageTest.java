begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
operator|.
name|test
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|i18n
operator|.
name|phonenumbers
operator|.
name|PhoneNumberUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|i18n
operator|.
name|phonenumbers
operator|.
name|PhoneNumberUtil
operator|.
name|PhoneNumberFormat
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
name|cm
operator|.
name|CMConstants
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
name|cm
operator|.
name|CMMessage
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|Assert
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
block|{
name|ValidatorConfiguration
operator|.
name|class
block|}
argument_list|)
comment|// @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
comment|// @DisableJmx(false)
comment|// @FixMethodOrder(MethodSorters.NAME_ASCENDING)
DECL|class|CMMessageTest
specifier|public
class|class
name|CMMessageTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|pnu
specifier|private
specifier|final
name|PhoneNumberUtil
name|pnu
init|=
name|PhoneNumberUtil
operator|.
name|getInstance
argument_list|()
decl_stmt|;
DECL|field|validNumber
specifier|private
name|String
name|validNumber
decl_stmt|;
annotation|@
name|Before
DECL|method|beforeTest ()
specifier|public
name|void
name|beforeTest
parameter_list|()
throws|throws
name|Exception
block|{
name|validNumber
operator|=
name|pnu
operator|.
name|format
argument_list|(
name|pnu
operator|.
name|getExampleNumber
argument_list|(
literal|"ES"
argument_list|)
argument_list|,
name|PhoneNumberFormat
operator|.
name|E164
argument_list|)
expr_stmt|;
block|}
comment|// @After
comment|// public void afterTest() {
comment|/*      * GSM0338      */
annotation|@
name|Test
DECL|method|testGSM338AndLTMAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndLTMAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 0338 and less than 160 char -> 1 part
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGSM338AndEQMAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndEQMAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 0338 and length is exactly 160 -> 1 part
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGSM338AndGTMAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndGTMAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 0338 and length is exactly 161 -> 2 part
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH
operator|+
literal|1
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGSM338AndLT2MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndLT2MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|2
operator|*
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|-
literal|1
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGSM338AndEQ2MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndEQ2MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|2
operator|*
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGSM338AndGT2MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndGT2MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|2
operator|*
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|+
literal|1
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|3
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGSM338AndEQ8MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndEQ8MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|8
operator|*
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|8
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGSM338AndGT8MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testGSM338AndGT8MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|8
operator|*
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|+
literal|1
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|8
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
operator|!
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*      * Unicode Messages      */
annotation|@
name|Test
DECL|method|testUnicodeAndLTMAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndLTMAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
comment|// 0338 and less than 160 char -> 1 part
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|CMConstants
operator|.
name|MAX_UNICODE_MESSAGE_LENGTH
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnicodeAndEQMAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndEQMAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 0338 and length is exactly 160 -> 1 part
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|CMConstants
operator|.
name|MAX_UNICODE_MESSAGE_LENGTH
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnicodeAndGTMAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndGTMAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 0338 and length is exactly 161 -> 2 part
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|CMConstants
operator|.
name|MAX_UNICODE_MESSAGE_LENGTH
operator|+
literal|1
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnicodeAndLT2MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndLT2MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|2
operator|*
name|CMConstants
operator|.
name|MAX_UNICODE_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|-
literal|1
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnicodeAndEQ2MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndEQ2MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|2
operator|*
name|CMConstants
operator|.
name|MAX_UNICODE_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnicodeAndGT2MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndGT2MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|2
operator|*
name|CMConstants
operator|.
name|MAX_UNICODE_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|+
literal|1
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|3
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnicodeAndEQ8MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndEQ8MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|8
operator|*
name|CMConstants
operator|.
name|MAX_UNICODE_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|8
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnicodeAndGT8MAXGSMMESSAGELENGTH ()
specifier|public
name|void
name|testUnicodeAndGT8MAXGSMMESSAGELENGTH
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ch
init|=
literal|"\uF400"
decl_stmt|;
name|StringBuffer
name|message
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
operator|(
literal|8
operator|*
name|CMConstants
operator|.
name|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
operator|+
literal|1
operator|)
condition|;
name|index
operator|++
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CMMessage
name|cmMessage
init|=
operator|new
name|CMMessage
argument_list|(
name|validNumber
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cmMessage
operator|.
name|setUnicodeAndMultipart
argument_list|(
name|CMConstants
operator|.
name|DEFAULT_MULTIPARTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|getMultiparts
argument_list|()
operator|==
literal|8
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|isTrue
argument_list|(
name|cmMessage
operator|.
name|isUnicode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


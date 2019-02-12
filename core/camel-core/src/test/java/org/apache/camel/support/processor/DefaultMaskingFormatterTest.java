begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|processor
package|;
end_package

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
DECL|class|DefaultMaskingFormatterTest
specifier|public
class|class
name|DefaultMaskingFormatterTest
block|{
annotation|@
name|Test
DECL|method|testDefaultOption ()
specifier|public
name|void
name|testDefaultOption
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultMaskingFormatter
name|formatter
init|=
operator|new
name|DefaultMaskingFormatter
argument_list|()
decl_stmt|;
name|String
name|answer
init|=
name|formatter
operator|.
name|format
argument_list|(
literal|"key=value, myPassword=foo,\n myPassphrase=\"foo bar\", secretKey='!@#$%^&*() -+[]{};:'"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"key=value, myPassword=\"xxxxx\",\n myPassphrase=\"xxxxx\", secretKey=\"xxxxx\""
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"<xmlPassword>\n foo bar \n</xmlPassword>\n<user password=\"asdf qwert\"/>"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"<xmlPassword>\n xxxxx \n</xmlPassword>\n<user password=\"xxxxx\"/>"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"foo\", \"My SecretPassphrase\" : \"foo bar\", \"My SecretKey2\" : \"!@#$%^&*() -+[]{};:'\"}"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"xxxxx\", \"My SecretPassphrase\" : \"xxxxx\", \"My SecretKey2\" : \"xxxxx\"}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisableKeyValueMask ()
specifier|public
name|void
name|testDisableKeyValueMask
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultMaskingFormatter
name|formatter
init|=
operator|new
name|DefaultMaskingFormatter
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|answer
init|=
name|formatter
operator|.
name|format
argument_list|(
literal|"key=value, myPassword=foo,\n myPassphrase=\"foo bar\", secretKey='!@#$%^&*() -+[]{};:'"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"key=value, myPassword=foo,\n myPassphrase=\"foo bar\", secretKey='!@#$%^&*() -+[]{};:'"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"<xmlPassword>\n foo bar \n</xmlPassword>\n<user password=\"asdf qwert\"/>"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"<xmlPassword>\n xxxxx \n</xmlPassword>\n<user password=\"asdf qwert\"/>"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"foo\", \"My SecretPassphrase\" : \"foo bar\", \"My SecretKey2\" : \"!@#$%^&*() -+[]{};:'\"}"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"xxxxx\", \"My SecretPassphrase\" : \"xxxxx\", \"My SecretKey2\" : \"xxxxx\"}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisableXmlElementMask ()
specifier|public
name|void
name|testDisableXmlElementMask
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultMaskingFormatter
name|formatter
init|=
operator|new
name|DefaultMaskingFormatter
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|answer
init|=
name|formatter
operator|.
name|format
argument_list|(
literal|"key=value, myPassword=foo,\n myPassphrase=\"foo bar\", secretKey='!@#$%^&*() -+[]{};:'"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"key=value, myPassword=\"xxxxx\",\n myPassphrase=\"xxxxx\", secretKey=\"xxxxx\""
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"<xmlPassword>\n foo bar \n</xmlPassword>\n<user password=\"asdf qwert\"/>"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"<xmlPassword>\n foo bar \n</xmlPassword>\n<user password=\"xxxxx\"/>"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"foo\", \"My SecretPassphrase\" : \"foo bar\", \"My SecretKey2\" : \"!@#$%^&*() -+[]{};:'\"}"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"xxxxx\", \"My SecretPassphrase\" : \"xxxxx\", \"My SecretKey2\" : \"xxxxx\"}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisableJsonMask ()
specifier|public
name|void
name|testDisableJsonMask
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultMaskingFormatter
name|formatter
init|=
operator|new
name|DefaultMaskingFormatter
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|answer
init|=
name|formatter
operator|.
name|format
argument_list|(
literal|"key=value, myPassword=foo,\n myPassphrase=\"fooãbar\", secretKey='!@#$%^&*() -+[]{};:'"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"key=value, myPassword=\"xxxxx\",\n myPassphrase=\"xxxxx\", secretKey=\"xxxxx\""
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"<xmlPassword>\n foo bar \n</xmlPassword>\n<user password=\"asdf qwert\"/>"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"<xmlPassword>\n xxxxx \n</xmlPassword>\n<user password=\"xxxxx\"/>"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"foo\", \"My SecretPassphrase\" : \"foo bar\", \"My SecretKey2\" : \"!@#$%^&*() -+[]{};:'\"}"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"foo\", \"My SecretPassphrase\" : \"foo bar\", \"My SecretKey2\" : \"!@#$%^&*() -+[]{};:'\"}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomMaskString ()
specifier|public
name|void
name|testCustomMaskString
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultMaskingFormatter
name|formatter
init|=
operator|new
name|DefaultMaskingFormatter
argument_list|()
decl_stmt|;
name|formatter
operator|.
name|setMaskString
argument_list|(
literal|"**********"
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
name|formatter
operator|.
name|format
argument_list|(
literal|"key=value, myPassword=foo,\n myPassphrase=\"fooãbar\", secretKey='!@#$%^&*() -+[]{};:'"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"key=value, myPassword=\"**********\",\n myPassphrase=\"**********\", secretKey=\"**********\""
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"<xmlPassword>\n foo bar \n</xmlPassword>\n<user password=\"asdf qwert\"/>"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"<xmlPassword>\n ********** \n</xmlPassword>\n<user password=\"**********\"/>"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|formatter
operator|.
name|format
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"foo\", \"My SecretPassphrase\" : \"foo bar\", \"My SecretKey2\" : \"!@#$%^&*() -+[]{};:'\"}"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"{\"key\" : \"value\", \"My Password\":\"**********\", \"My SecretPassphrase\" : \"**********\", \"My SecretKey2\" : \"**********\"}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

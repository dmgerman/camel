begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jasypt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jasypt
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jasypt
operator|.
name|encryption
operator|.
name|pbe
operator|.
name|StandardPBEStringEncryptor
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jasypt
operator|.
name|JasyptPropertiesParser
operator|.
name|JASYPT_PREFIX_TOKEN
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jasypt
operator|.
name|JasyptPropertiesParser
operator|.
name|JASYPT_SUFFIX_TOKEN
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|JasyptPropertiesParserTest
specifier|public
class|class
name|JasyptPropertiesParserTest
block|{
DECL|field|KEY
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"somekey"
decl_stmt|;
DECL|field|KNOWN_PASSWORD
specifier|private
specifier|static
specifier|final
name|String
name|KNOWN_PASSWORD
init|=
literal|"secret"
decl_stmt|;
DECL|field|KNOWN_ENCRYPTED
specifier|private
specifier|static
specifier|final
name|String
name|KNOWN_ENCRYPTED
init|=
literal|"ENC(bsW9uV37gQ0QHFu7KO03Ww==)"
decl_stmt|;
DECL|field|KNOW_DECRYPTED
specifier|private
specifier|static
specifier|final
name|String
name|KNOW_DECRYPTED
init|=
literal|"tiger"
decl_stmt|;
DECL|field|jasyptPropertiesParser
specifier|private
name|JasyptPropertiesParser
name|jasyptPropertiesParser
init|=
operator|new
name|JasyptPropertiesParser
argument_list|()
decl_stmt|;
DECL|field|encryptor
specifier|private
name|StandardPBEStringEncryptor
name|encryptor
init|=
operator|new
name|StandardPBEStringEncryptor
argument_list|()
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|encryptor
operator|.
name|setPassword
argument_list|(
name|KNOWN_PASSWORD
argument_list|)
expr_stmt|;
name|jasyptPropertiesParser
operator|.
name|setEncryptor
argument_list|(
name|encryptor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullPropertyIsUntouched ()
specifier|public
name|void
name|testNullPropertyIsUntouched
parameter_list|()
block|{
name|String
name|expected
init|=
literal|null
decl_stmt|;
name|String
name|result
init|=
name|jasyptPropertiesParser
operator|.
name|parseProperty
argument_list|(
name|KEY
argument_list|,
name|expected
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPlainPropertyIsUntouched ()
specifier|public
name|void
name|testPlainPropertyIsUntouched
parameter_list|()
block|{
name|String
name|expected
init|=
literal|"http://somehost?1=someval1&2=someval2"
decl_stmt|;
name|String
name|result
init|=
name|jasyptPropertiesParser
operator|.
name|parseProperty
argument_list|(
name|KEY
argument_list|,
name|expected
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDecryptsEncryptedProperty ()
specifier|public
name|void
name|testDecryptsEncryptedProperty
parameter_list|()
block|{
name|String
name|decrypted
init|=
literal|"tiger"
decl_stmt|;
name|String
name|encrypted
init|=
name|format
argument_list|(
literal|"%s%s%s"
argument_list|,
name|JASYPT_PREFIX_TOKEN
argument_list|,
name|encryptor
operator|.
name|encrypt
argument_list|(
name|decrypted
argument_list|)
argument_list|,
name|JASYPT_SUFFIX_TOKEN
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|jasyptPropertiesParser
operator|.
name|parseProperty
argument_list|(
name|KEY
argument_list|,
name|encrypted
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|decrypted
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDecryptsPartiallyEncryptedProperty ()
specifier|public
name|void
name|testDecryptsPartiallyEncryptedProperty
parameter_list|()
block|{
name|String
name|parmValue
init|=
literal|"tiger"
decl_stmt|;
name|String
name|encParmValue
init|=
name|format
argument_list|(
literal|"%s%s%s"
argument_list|,
name|JASYPT_PREFIX_TOKEN
argument_list|,
name|encryptor
operator|.
name|encrypt
argument_list|(
name|parmValue
argument_list|)
argument_list|,
name|JASYPT_SUFFIX_TOKEN
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|format
argument_list|(
literal|"http://somehost:port/?param1=%s&param2=somethingelse"
argument_list|,
name|parmValue
argument_list|)
decl_stmt|;
name|String
name|propertyValue
init|=
name|format
argument_list|(
literal|"http://somehost:port/?param1=%s&param2=somethingelse"
argument_list|,
name|encParmValue
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|jasyptPropertiesParser
operator|.
name|parseProperty
argument_list|(
name|KEY
argument_list|,
name|propertyValue
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDecryptsMultitplePartsOfPartiallyEncryptedProperty ()
specifier|public
name|void
name|testDecryptsMultitplePartsOfPartiallyEncryptedProperty
parameter_list|()
block|{
name|StringBuilder
name|propertyValue
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|expected
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|propertyValue
operator|.
name|append
argument_list|(
name|format
argument_list|(
literal|"param%s=%s%s%s()&"
argument_list|,
name|i
argument_list|,
name|JASYPT_PREFIX_TOKEN
argument_list|,
name|encryptor
operator|.
name|encrypt
argument_list|(
literal|"tiger"
operator|+
name|i
argument_list|)
argument_list|,
name|JASYPT_SUFFIX_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
name|expected
operator|.
name|append
argument_list|(
name|format
argument_list|(
literal|"param%s=tiger%s()&"
argument_list|,
name|i
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|result
init|=
name|jasyptPropertiesParser
operator|.
name|parseProperty
argument_list|(
name|KEY
argument_list|,
name|propertyValue
operator|.
name|toString
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
name|expected
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUsesProvidedPasswordIfEncryptorIsNotSet ()
specifier|public
name|void
name|testUsesProvidedPasswordIfEncryptorIsNotSet
parameter_list|()
throws|throws
name|Exception
block|{
name|jasyptPropertiesParser
operator|.
name|setEncryptor
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|jasyptPropertiesParser
operator|.
name|setPassword
argument_list|(
name|KNOWN_PASSWORD
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KNOW_DECRYPTED
argument_list|,
name|jasyptPropertiesParser
operator|.
name|parseProperty
argument_list|(
name|KEY
argument_list|,
name|KNOWN_ENCRYPTED
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUsesProvidedPasswordFromSystemPropertyIfEncryptorIsNotSet ()
specifier|public
name|void
name|testUsesProvidedPasswordFromSystemPropertyIfEncryptorIsNotSet
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"myfoo"
argument_list|,
name|KNOWN_PASSWORD
argument_list|)
expr_stmt|;
name|jasyptPropertiesParser
operator|.
name|setEncryptor
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|jasyptPropertiesParser
operator|.
name|setPassword
argument_list|(
literal|"sys:myfoo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KNOW_DECRYPTED
argument_list|,
name|jasyptPropertiesParser
operator|.
name|parseProperty
argument_list|(
name|KEY
argument_list|,
name|KNOWN_ENCRYPTED
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"myfoo"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


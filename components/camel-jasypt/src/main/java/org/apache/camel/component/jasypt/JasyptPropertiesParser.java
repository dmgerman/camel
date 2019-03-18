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
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
operator|.
name|DefaultPropertiesParser
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
name|ObjectHelper
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
name|StringHelper
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
name|StringEncryptor
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

begin_comment
comment|/**  * A {@link org.apache.camel.component.properties.PropertiesParser} which is using  *&nbsp;<a href="http://www.jasypt.org/">Jasypt</a> to decrypt encrypted values.  *<p/>  * The parts of the values which should be decrypted must be enclosed in the prefix and suffix token.  */
end_comment

begin_class
DECL|class|JasyptPropertiesParser
specifier|public
class|class
name|JasyptPropertiesParser
extends|extends
name|DefaultPropertiesParser
block|{
DECL|field|JASYPT_PREFIX_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|JASYPT_PREFIX_TOKEN
init|=
literal|"ENC("
decl_stmt|;
DECL|field|JASYPT_SUFFIX_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|JASYPT_SUFFIX_TOKEN
init|=
literal|")"
decl_stmt|;
DECL|field|JASYPT_REGEX
specifier|private
specifier|static
specifier|final
name|String
name|JASYPT_REGEX
init|=
name|JASYPT_PREFIX_TOKEN
operator|.
name|replace
argument_list|(
literal|"("
argument_list|,
literal|"\\("
argument_list|)
operator|+
literal|"(.+?)"
operator|+
name|JASYPT_SUFFIX_TOKEN
operator|.
name|replace
argument_list|(
literal|")"
argument_list|,
literal|"\\)"
argument_list|)
decl_stmt|;
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|JASYPT_REGEX
argument_list|)
decl_stmt|;
DECL|field|encryptor
specifier|private
name|StringEncryptor
name|encryptor
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|algorithm
specifier|private
name|String
name|algorithm
decl_stmt|;
DECL|method|JasyptPropertiesParser ()
specifier|public
name|JasyptPropertiesParser
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|parseProperty (String key, String value, Properties properties)
specifier|public
name|String
name|parseProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|format
argument_list|(
literal|"Parsing property '%s=%s'"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|initEncryptor
argument_list|()
expr_stmt|;
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|value
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|format
argument_list|(
literal|"Decrypting part '%s'"
argument_list|,
name|matcher
operator|.
name|group
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|decrypted
init|=
name|encryptor
operator|.
name|decrypt
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|value
operator|=
name|value
operator|.
name|replace
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|0
argument_list|)
argument_list|,
name|decrypted
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|value
return|;
block|}
DECL|method|initEncryptor ()
specifier|private
specifier|synchronized
name|void
name|initEncryptor
parameter_list|()
block|{
if|if
condition|(
name|encryptor
operator|==
literal|null
condition|)
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|StandardPBEStringEncryptor
name|pbeStringEncryptor
init|=
operator|new
name|StandardPBEStringEncryptor
argument_list|()
decl_stmt|;
name|pbeStringEncryptor
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
if|if
condition|(
name|algorithm
operator|!=
literal|null
condition|)
block|{
name|pbeStringEncryptor
operator|.
name|setAlgorithm
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Initialized encryptor using %s algorithm and provided password"
argument_list|,
name|algorithm
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Initialized encryptor using default algorithm and provided password"
argument_list|)
expr_stmt|;
block|}
name|encryptor
operator|=
name|pbeStringEncryptor
expr_stmt|;
block|}
block|}
DECL|method|setEncryptor (StringEncryptor encryptor)
specifier|public
name|void
name|setEncryptor
parameter_list|(
name|StringEncryptor
name|encryptor
parameter_list|)
block|{
name|this
operator|.
name|encryptor
operator|=
name|encryptor
expr_stmt|;
block|}
DECL|method|setAlgorithm (String algorithm)
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
comment|// lookup password as either environment or JVM system property
if|if
condition|(
name|password
operator|.
name|startsWith
argument_list|(
literal|"sysenv:"
argument_list|)
condition|)
block|{
name|password
operator|=
name|System
operator|.
name|getenv
argument_list|(
name|StringHelper
operator|.
name|after
argument_list|(
name|password
argument_list|,
literal|"sysenv:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|password
argument_list|)
operator|&&
name|password
operator|.
name|startsWith
argument_list|(
literal|"sys:"
argument_list|)
condition|)
block|{
name|password
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|StringHelper
operator|.
name|after
argument_list|(
name|password
argument_list|,
literal|"sys:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
block|}
end_class

end_unit


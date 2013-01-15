begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A {@link org.apache.camel.component.properties.PropertiesParser} which is using  *<a href="http://www.jasypt.org/">Jasypt</a> to decrypt any encrypted values.  *<p/>  * The values must be enclosed in the prefix and suffix token.  *  * @version   */
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
DECL|field|encryptor
specifier|private
name|StandardPBEStringEncryptor
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
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
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
name|ObjectHelper
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
name|ObjectHelper
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
DECL|method|getAlgorithm ()
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
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
DECL|method|getEncryptor ()
specifier|public
specifier|synchronized
name|StandardPBEStringEncryptor
name|getEncryptor
parameter_list|()
block|{
if|if
condition|(
name|encryptor
operator|==
literal|null
condition|)
block|{
comment|// password is mandatory
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
literal|"password"
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|encryptor
operator|=
operator|new
name|StandardPBEStringEncryptor
argument_list|()
expr_stmt|;
name|encryptor
operator|.
name|setPassword
argument_list|(
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|algorithm
operator|!=
literal|null
condition|)
block|{
name|encryptor
operator|.
name|setAlgorithm
argument_list|(
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|encryptor
return|;
block|}
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
comment|// check if the value is using the tokens
name|String
name|text
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|value
argument_list|,
name|JASYPT_PREFIX_TOKEN
argument_list|,
name|JASYPT_SUFFIX_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
comment|// not encrypted
name|log
operator|.
name|trace
argument_list|(
literal|"Property is not encrypted {}"
argument_list|,
name|text
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Decrypting property {}"
argument_list|,
name|text
argument_list|)
expr_stmt|;
comment|// do not log the decrypted text as it could be sensitive information such as a password
return|return
name|getEncryptor
argument_list|()
operator|.
name|decrypt
argument_list|(
name|text
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


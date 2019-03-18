begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm.validation.constraints
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
name|validation
operator|.
name|constraints
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintValidator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintValidatorContext
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
name|NumberParseException
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
name|com
operator|.
name|google
operator|.
name|i18n
operator|.
name|phonenumbers
operator|.
name|Phonenumber
operator|.
name|PhoneNumber
import|;
end_import

begin_comment
comment|/**  * Checks that a given character sequence (e.g. string) is a valid E164 formatted phonenumber. https://www.cmtelecom.com/newsroom/how-to-format-international-telephone- numbers  * https://github.com/googlei18n/libphonenumber  */
end_comment

begin_class
DECL|class|E164Validator
specifier|public
class|class
name|E164Validator
implements|implements
name|ConstraintValidator
argument_list|<
name|E164
argument_list|,
name|String
argument_list|>
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
annotation|@
name|Override
DECL|method|initialize (final E164 constraintAnnotation)
specifier|public
name|void
name|initialize
parameter_list|(
specifier|final
name|E164
name|constraintAnnotation
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|isValid (final String value, final ConstraintValidatorContext context)
specifier|public
name|boolean
name|isValid
parameter_list|(
specifier|final
name|String
name|value
parameter_list|,
specifier|final
name|ConstraintValidatorContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
specifier|final
name|PhoneNumber
name|parsingResult
init|=
name|pnu
operator|.
name|parse
argument_list|(
name|value
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pnu
operator|.
name|format
argument_list|(
name|parsingResult
argument_list|,
name|PhoneNumberFormat
operator|.
name|E164
argument_list|)
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|NumberParseException
name|t
parameter_list|)
block|{
comment|// Errors when parsing phonenumber
return|return
literal|false
return|;
block|}
comment|// CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN
comment|// log.debug("Phone Number: {}", value);
comment|// log.debug("Country code: {}", numberProto.getCountryCode());
comment|// log.debug("National Number: {}", numberProto.getNationalNumber());
comment|// log.debug("E164 format: {}", pnu.format(numberProto,
comment|// PhoneNumberFormat.E164));
block|}
block|}
end_class

end_unit


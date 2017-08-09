begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintViolation
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
name|CamelExecutionException
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
name|Processor
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
name|junit4
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
name|After
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|BeanValidatorRouteTest
specifier|public
class|class
name|BeanValidatorRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|origLocale
specifier|private
name|Locale
name|origLocale
decl_stmt|;
annotation|@
name|Before
DECL|method|setLanguage ()
specifier|public
name|void
name|setLanguage
parameter_list|()
block|{
name|origLocale
operator|=
name|Locale
operator|.
name|getDefault
argument_list|()
expr_stmt|;
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|restoreLanguage ()
specifier|public
name|void
name|restoreLanguage
parameter_list|()
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|origLocale
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldSuccessWithImpliciteDefaultGroup ()
specifier|public
name|void
name|validateShouldSuccessWithImpliciteDefaultGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"bean-validator://x"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|createCar
argument_list|(
literal|"BMW"
argument_list|,
literal|"DD-AB-123"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldSuccessWithExpliciteDefaultGroup ()
specifier|public
name|void
name|validateShouldSuccessWithExpliciteDefaultGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"bean-validator://x?group=javax.validation.groups.Default"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|createCar
argument_list|(
literal|"BMW"
argument_list|,
literal|"DD-AB-123"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldFailWithImpliciteDefaultGroup ()
specifier|public
name|void
name|validateShouldFailWithImpliciteDefaultGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
specifier|final
name|String
name|url
init|=
literal|"bean-validator://x"
decl_stmt|;
specifier|final
name|Car
name|car
init|=
name|createCar
argument_list|(
literal|"BMW"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|url
argument_list|,
name|car
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|BeanValidationException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationException
name|exception
init|=
operator|(
name|BeanValidationException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|exception
operator|.
name|getConstraintViolations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constraintViolations
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
name|constraintViolation
init|=
name|constraintViolations
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"licensePlate"
argument_list|,
name|constraintViolation
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|constraintViolation
operator|.
name|getInvalidValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"must not be null"
argument_list|,
name|constraintViolation
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|car
operator|.
name|setLicensePlate
argument_list|(
literal|"D-A"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|url
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|car
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldFailWithExpliciteDefaultGroup ()
specifier|public
name|void
name|validateShouldFailWithExpliciteDefaultGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
specifier|final
name|String
name|url
init|=
literal|"bean-validator://x?group=javax.validation.groups.Default"
decl_stmt|;
specifier|final
name|Car
name|car
init|=
name|createCar
argument_list|(
literal|"BMW"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|url
argument_list|,
name|car
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|BeanValidationException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationException
name|exception
init|=
operator|(
name|BeanValidationException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|exception
operator|.
name|getConstraintViolations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constraintViolations
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
name|constraintViolation
init|=
name|constraintViolations
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"licensePlate"
argument_list|,
name|constraintViolation
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|constraintViolation
operator|.
name|getInvalidValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"must not be null"
argument_list|,
name|constraintViolation
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|car
operator|.
name|setLicensePlate
argument_list|(
literal|"D-A"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|url
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|car
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldFailWithOptionalChecksGroup ()
specifier|public
name|void
name|validateShouldFailWithOptionalChecksGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
specifier|final
name|String
name|url
init|=
literal|"bean-validator://x?group=org.apache.camel.component.bean.validator.OptionalChecks"
decl_stmt|;
specifier|final
name|Car
name|car
init|=
name|createCar
argument_list|(
literal|"BMW"
argument_list|,
literal|"D-A"
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|url
argument_list|,
name|car
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|BeanValidationException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationException
name|exception
init|=
operator|(
name|BeanValidationException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|exception
operator|.
name|getConstraintViolations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constraintViolations
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
name|constraintViolation
init|=
name|constraintViolations
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"licensePlate"
argument_list|,
name|constraintViolation
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"D-A"
argument_list|,
name|constraintViolation
operator|.
name|getInvalidValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"size must be between 5 and 14"
argument_list|,
name|constraintViolation
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|car
operator|.
name|setLicensePlate
argument_list|(
literal|"DD-AB-123"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|url
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|car
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldFailWithOrderedChecksGroup ()
specifier|public
name|void
name|validateShouldFailWithOrderedChecksGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
specifier|final
name|String
name|url
init|=
literal|"bean-validator://x?group=org.apache.camel.component.bean.validator.OrderedChecks"
decl_stmt|;
specifier|final
name|Car
name|car
init|=
name|createCar
argument_list|(
literal|null
argument_list|,
literal|"D-A"
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|url
argument_list|,
name|car
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|BeanValidationException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationException
name|exception
init|=
operator|(
name|BeanValidationException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|exception
operator|.
name|getConstraintViolations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constraintViolations
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
name|constraintViolation
init|=
name|constraintViolations
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"manufacturer"
argument_list|,
name|constraintViolation
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|constraintViolation
operator|.
name|getInvalidValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"must not be null"
argument_list|,
name|constraintViolation
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|car
operator|.
name|setManufacturer
argument_list|(
literal|"BMW"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|url
argument_list|,
name|car
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|BeanValidationException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationException
name|exception
init|=
operator|(
name|BeanValidationException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|exception
operator|.
name|getConstraintViolations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constraintViolations
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
name|constraintViolation
init|=
name|constraintViolations
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"licensePlate"
argument_list|,
name|constraintViolation
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"D-A"
argument_list|,
name|constraintViolation
operator|.
name|getInvalidValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"size must be between 5 and 14"
argument_list|,
name|constraintViolation
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|car
operator|.
name|setLicensePlate
argument_list|(
literal|"DD-AB-123"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|url
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|car
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldSuccessWithRedefinedDefaultGroup ()
specifier|public
name|void
name|validateShouldSuccessWithRedefinedDefaultGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
specifier|final
name|String
name|url
init|=
literal|"bean-validator://x"
decl_stmt|;
specifier|final
name|Car
name|car
init|=
operator|new
name|CarWithRedefinedDefaultGroup
argument_list|(
literal|null
argument_list|,
literal|"DD-AB-123"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|url
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|car
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateShouldFailWithRedefinedDefaultGroup ()
specifier|public
name|void
name|validateShouldFailWithRedefinedDefaultGroup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
comment|// cannot run on aix
return|return;
block|}
specifier|final
name|String
name|url
init|=
literal|"bean-validator://x"
decl_stmt|;
specifier|final
name|Car
name|car
init|=
operator|new
name|CarWithRedefinedDefaultGroup
argument_list|(
literal|null
argument_list|,
literal|"D-A"
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|url
argument_list|,
name|car
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|BeanValidationException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationException
name|exception
init|=
operator|(
name|BeanValidationException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|exception
operator|.
name|getConstraintViolations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constraintViolations
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ConstraintViolation
argument_list|<
name|Object
argument_list|>
name|constraintViolation
init|=
name|constraintViolations
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"licensePlate"
argument_list|,
name|constraintViolation
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"D-A"
argument_list|,
name|constraintViolation
operator|.
name|getInvalidValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"size must be between 5 and 14"
argument_list|,
name|constraintViolation
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createCar (String manufacturer, String licencePlate)
name|Car
name|createCar
parameter_list|(
name|String
name|manufacturer
parameter_list|,
name|String
name|licencePlate
parameter_list|)
block|{
return|return
operator|new
name|CarWithAnnotations
argument_list|(
name|manufacturer
argument_list|,
name|licencePlate
argument_list|)
return|;
block|}
block|}
end_class

end_unit


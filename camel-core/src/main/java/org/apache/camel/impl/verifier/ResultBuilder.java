begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|verifier
package|;
end_package

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
name|Collections
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
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|ComponentVerifier
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
name|IllegalOptionException
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
name|NoSuchOptionException
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
name|function
operator|.
name|ThrowingBiConsumer
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
name|function
operator|.
name|ThrowingConsumer
import|;
end_import

begin_class
DECL|class|ResultBuilder
specifier|public
specifier|final
class|class
name|ResultBuilder
block|{
DECL|field|scope
specifier|private
name|Optional
argument_list|<
name|ComponentVerifier
operator|.
name|Scope
argument_list|>
name|scope
decl_stmt|;
DECL|field|status
specifier|private
name|Optional
argument_list|<
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
argument_list|>
name|status
decl_stmt|;
DECL|field|verificationErrors
specifier|private
name|List
argument_list|<
name|ComponentVerifier
operator|.
name|VerificationError
argument_list|>
name|verificationErrors
decl_stmt|;
DECL|method|ResultBuilder ()
specifier|public
name|ResultBuilder
parameter_list|()
block|{
name|this
operator|.
name|scope
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|scope
operator|.
name|empty
argument_list|()
expr_stmt|;
block|}
comment|// **********************************
comment|// Accessors
comment|// **********************************
DECL|method|scope (ComponentVerifier.Scope scope)
specifier|public
name|ResultBuilder
name|scope
parameter_list|(
name|ComponentVerifier
operator|.
name|Scope
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|scope
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|status (ComponentVerifier.Result.Status status)
specifier|public
name|ResultBuilder
name|status
parameter_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|status
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|error (ComponentVerifier.VerificationError verificationError)
specifier|public
name|ResultBuilder
name|error
parameter_list|(
name|ComponentVerifier
operator|.
name|VerificationError
name|verificationError
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|verificationErrors
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|verificationErrors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|verificationErrors
operator|.
name|add
argument_list|(
name|verificationError
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|ERROR
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|error (Optional<ComponentVerifier.VerificationError> error)
specifier|public
name|ResultBuilder
name|error
parameter_list|(
name|Optional
argument_list|<
name|ComponentVerifier
operator|.
name|VerificationError
argument_list|>
name|error
parameter_list|)
block|{
name|error
operator|.
name|ifPresent
argument_list|(
name|e
lambda|->
name|error
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|error (Supplier<Optional<ComponentVerifier.VerificationError>> supplier)
specifier|public
name|ResultBuilder
name|error
parameter_list|(
name|Supplier
argument_list|<
name|Optional
argument_list|<
name|ComponentVerifier
operator|.
name|VerificationError
argument_list|>
argument_list|>
name|supplier
parameter_list|)
block|{
return|return
name|error
argument_list|(
name|supplier
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|error (ThrowingConsumer<ResultBuilder, Exception> consumer)
specifier|public
name|ResultBuilder
name|error
parameter_list|(
name|ThrowingConsumer
argument_list|<
name|ResultBuilder
argument_list|,
name|Exception
argument_list|>
name|consumer
parameter_list|)
block|{
try|try
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchOptionException
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
name|e
operator|.
name|getOptionName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalOptionException
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withIllegalOption
argument_list|(
name|e
operator|.
name|getOptionName
argument_list|()
argument_list|,
name|e
operator|.
name|getOptionValue
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withException
argument_list|(
name|e
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|error (T data, ThrowingBiConsumer<ResultBuilder, T, Exception> consumer)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ResultBuilder
name|error
parameter_list|(
name|T
name|data
parameter_list|,
name|ThrowingBiConsumer
argument_list|<
name|ResultBuilder
argument_list|,
name|T
argument_list|,
name|Exception
argument_list|>
name|consumer
parameter_list|)
block|{
try|try
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|this
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchOptionException
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
name|e
operator|.
name|getOptionName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalOptionException
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withIllegalOption
argument_list|(
name|e
operator|.
name|getOptionName
argument_list|()
argument_list|,
name|e
operator|.
name|getOptionValue
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withException
argument_list|(
name|e
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|errors (List<ComponentVerifier.VerificationError> verificationErrors)
specifier|public
name|ResultBuilder
name|errors
parameter_list|(
name|List
argument_list|<
name|ComponentVerifier
operator|.
name|VerificationError
argument_list|>
name|verificationErrors
parameter_list|)
block|{
name|verificationErrors
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|error
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// **********************************
comment|// Build
comment|// **********************************
DECL|method|build ()
specifier|public
name|ComponentVerifier
operator|.
name|Result
name|build
parameter_list|()
block|{
return|return
operator|new
name|DefaultResult
argument_list|(
name|scope
operator|.
name|orElse
argument_list|(
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|)
argument_list|,
name|status
operator|.
name|orElse
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|UNSUPPORTED
argument_list|)
argument_list|,
name|verificationErrors
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|verificationErrors
argument_list|)
else|:
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
comment|// **********************************
comment|// Helpers
comment|// **********************************
DECL|method|withStatus (ComponentVerifier.Result.Status status)
specifier|public
specifier|static
name|ResultBuilder
name|withStatus
parameter_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
name|status
parameter_list|)
block|{
return|return
operator|new
name|ResultBuilder
argument_list|()
operator|.
name|status
argument_list|(
name|status
argument_list|)
return|;
block|}
DECL|method|withStatusAndScope (ComponentVerifier.Result.Status status, ComponentVerifier.Scope scope)
specifier|public
specifier|static
name|ResultBuilder
name|withStatusAndScope
parameter_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
name|status
parameter_list|,
name|ComponentVerifier
operator|.
name|Scope
name|scope
parameter_list|)
block|{
return|return
operator|new
name|ResultBuilder
argument_list|()
operator|.
name|status
argument_list|(
name|status
argument_list|)
operator|.
name|scope
argument_list|(
name|scope
argument_list|)
return|;
block|}
DECL|method|withScope (ComponentVerifier.Scope scope)
specifier|public
specifier|static
name|ResultBuilder
name|withScope
parameter_list|(
name|ComponentVerifier
operator|.
name|Scope
name|scope
parameter_list|)
block|{
return|return
operator|new
name|ResultBuilder
argument_list|()
operator|.
name|scope
argument_list|(
name|scope
argument_list|)
return|;
block|}
DECL|method|unsupported ()
specifier|public
specifier|static
name|ResultBuilder
name|unsupported
parameter_list|()
block|{
return|return
name|withStatusAndScope
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|UNSUPPORTED
argument_list|,
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|)
return|;
block|}
block|}
end_class

end_unit


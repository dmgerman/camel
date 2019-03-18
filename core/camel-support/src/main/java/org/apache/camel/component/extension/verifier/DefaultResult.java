begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
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
name|List
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
name|extension
operator|.
name|ComponentVerifierExtension
import|;
end_import

begin_class
DECL|class|DefaultResult
specifier|public
class|class
name|DefaultResult
implements|implements
name|ComponentVerifierExtension
operator|.
name|Result
block|{
DECL|field|scope
specifier|private
specifier|final
name|ComponentVerifierExtension
operator|.
name|Scope
name|scope
decl_stmt|;
DECL|field|status
specifier|private
specifier|final
name|Status
name|status
decl_stmt|;
DECL|field|verificationErrors
specifier|private
specifier|final
name|List
argument_list|<
name|ComponentVerifierExtension
operator|.
name|VerificationError
argument_list|>
name|verificationErrors
decl_stmt|;
DECL|method|DefaultResult (ComponentVerifierExtension.Scope scope, Status status, List<ComponentVerifierExtension.VerificationError> verificationErrors)
specifier|public
name|DefaultResult
parameter_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
name|scope
parameter_list|,
name|Status
name|status
parameter_list|,
name|List
argument_list|<
name|ComponentVerifierExtension
operator|.
name|VerificationError
argument_list|>
name|verificationErrors
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|this
operator|.
name|verificationErrors
operator|=
name|verificationErrors
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScope ()
specifier|public
name|ComponentVerifierExtension
operator|.
name|Scope
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
annotation|@
name|Override
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
annotation|@
name|Override
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|ComponentVerifierExtension
operator|.
name|VerificationError
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|verificationErrors
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DefaultResult{"
operator|+
literal|"scope="
operator|+
name|scope
operator|+
literal|", status="
operator|+
name|status
operator|+
literal|", errors="
operator|+
name|verificationErrors
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit


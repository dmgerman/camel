begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
package|;
end_package

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|HL7Exception
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|model
operator|.
name|Message
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
name|support
operator|.
name|ExpressionAdapter
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

begin_class
DECL|class|AckExpression
specifier|public
class|class
name|AckExpression
extends|extends
name|ExpressionAdapter
block|{
DECL|field|acknowledgementCode
specifier|private
name|AckCode
name|acknowledgementCode
decl_stmt|;
DECL|field|errorMessage
specifier|private
name|String
name|errorMessage
decl_stmt|;
DECL|field|errorCode
specifier|private
name|int
name|errorCode
init|=
name|HL7Exception
operator|.
name|APPLICATION_INTERNAL_ERROR
decl_stmt|;
DECL|method|AckExpression ()
specifier|public
name|AckExpression
parameter_list|()
block|{     }
DECL|method|AckExpression (AckCode acknowledgementCode)
specifier|public
name|AckExpression
parameter_list|(
name|AckCode
name|acknowledgementCode
parameter_list|)
block|{
name|this
operator|.
name|acknowledgementCode
operator|=
name|acknowledgementCode
expr_stmt|;
block|}
DECL|method|AckExpression (AckCode acknowledgementCode, String errorMessage, int errorCode)
specifier|public
name|AckExpression
parameter_list|(
name|AckCode
name|acknowledgementCode
parameter_list|,
name|String
name|errorMessage
parameter_list|,
name|int
name|errorCode
parameter_list|)
block|{
name|this
argument_list|(
name|acknowledgementCode
argument_list|)
expr_stmt|;
name|this
operator|.
name|errorMessage
operator|=
name|errorMessage
expr_stmt|;
name|this
operator|.
name|errorCode
operator|=
name|errorCode
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Throwable
name|t
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|HL7Exception
name|hl7e
init|=
name|generateHL7Exception
argument_list|(
name|t
argument_list|)
decl_stmt|;
name|AckCode
name|code
init|=
name|acknowledgementCode
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
operator|&&
name|code
operator|==
literal|null
condition|)
block|{
name|code
operator|=
name|AckCode
operator|.
name|AE
expr_stmt|;
block|}
return|return
name|msg
operator|.
name|generateACK
argument_list|(
name|code
operator|==
literal|null
condition|?
literal|null
else|:
name|code
operator|.
name|name
argument_list|()
argument_list|,
name|hl7e
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|generateHL7Exception (Throwable t)
specifier|private
name|HL7Exception
name|generateHL7Exception
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|HL7Exception
name|hl7Exception
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|acknowledgementCode
operator|!=
literal|null
operator|&&
name|acknowledgementCode
operator|.
name|isError
argument_list|()
condition|)
block|{
name|hl7Exception
operator|=
operator|new
name|HL7Exception
argument_list|(
name|errorMessage
argument_list|,
name|errorCode
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|t
operator|instanceof
name|HL7Exception
condition|)
block|{
name|hl7Exception
operator|=
operator|(
name|HL7Exception
operator|)
name|t
expr_stmt|;
block|}
else|else
block|{
name|hl7Exception
operator|=
operator|new
name|HL7Exception
argument_list|(
name|errorMessage
operator|!=
literal|null
condition|?
name|errorMessage
else|:
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|errorCode
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|hl7Exception
return|;
block|}
block|}
end_class

end_unit


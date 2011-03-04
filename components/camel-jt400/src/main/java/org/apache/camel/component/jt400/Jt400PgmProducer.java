begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
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
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400Message
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400Text
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|ProgramCall
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|ProgramParameter
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
name|InvalidPayloadException
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
name|impl
operator|.
name|DefaultProducer
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|Jt400PgmProducer
specifier|public
class|class
name|Jt400PgmProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Jt400PgmProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|Jt400PgmProducer (Jt400PgmEndpoint endpoint)
specifier|public
name|Jt400PgmProducer
parameter_list|(
name|Jt400PgmEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|getISeriesEndpoint ()
specifier|private
name|Jt400PgmEndpoint
name|getISeriesEndpoint
parameter_list|()
block|{
return|return
operator|(
name|Jt400PgmEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|process (Exchange exchange)
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
name|AS400
name|iSeries
init|=
name|getISeriesEndpoint
argument_list|()
operator|.
name|getiSeries
argument_list|()
decl_stmt|;
name|String
name|commandStr
init|=
name|getISeriesEndpoint
argument_list|()
operator|.
name|getProgramToExecute
argument_list|()
decl_stmt|;
name|ProgramParameter
index|[]
name|parameterList
init|=
name|getParameterList
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|ProgramCall
name|pgmCall
init|=
operator|new
name|ProgramCall
argument_list|(
name|iSeries
argument_list|)
decl_stmt|;
name|pgmCall
operator|.
name|setProgram
argument_list|(
name|commandStr
argument_list|)
expr_stmt|;
name|pgmCall
operator|.
name|setParameterList
argument_list|(
name|parameterList
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting to call PGM '"
operator|+
name|commandStr
operator|+
literal|"' in host '"
operator|+
name|iSeries
operator|.
name|getSystemName
argument_list|()
operator|+
literal|"' authentication with the user '"
operator|+
name|iSeries
operator|.
name|getUserId
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|result
init|=
name|pgmCall
operator|.
name|run
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Executed PGM '"
operator|+
name|commandStr
operator|+
literal|"' in host '"
operator|+
name|iSeries
operator|.
name|getSystemName
argument_list|()
operator|+
literal|"'. Success? "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
condition|)
block|{
name|handlePGMOutput
argument_list|(
name|exchange
argument_list|,
name|pgmCall
argument_list|,
name|parameterList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Jt400PgmCallException
argument_list|(
name|getOutputMessages
argument_list|(
name|pgmCall
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|getParameterList (Exchange exchange)
specifier|private
name|ProgramParameter
index|[]
name|getParameterList
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Object
name|body
init|=
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
index|[]
name|params
init|=
operator|(
name|String
index|[]
operator|)
name|body
decl_stmt|;
name|ProgramParameter
index|[]
name|parameterList
init|=
operator|new
name|ProgramParameter
index|[
name|params
operator|.
name|length
index|]
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
name|params
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|param
init|=
name|params
index|[
name|i
index|]
decl_stmt|;
name|boolean
name|input
init|=
name|param
operator|!=
literal|null
decl_stmt|;
name|boolean
name|output
init|=
name|getISeriesEndpoint
argument_list|()
operator|.
name|isFieldIdxForOuput
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|byte
index|[]
name|inputData
init|=
literal|null
decl_stmt|;
name|int
name|outputLength
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|input
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|param
decl_stmt|;
name|inputData
operator|=
operator|new
name|AS400Text
argument_list|(
name|value
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|toBytes
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|output
condition|)
block|{
name|outputLength
operator|=
name|getISeriesEndpoint
argument_list|()
operator|.
name|getOutputFieldLength
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|&&
name|output
condition|)
block|{
name|parameterList
index|[
name|i
index|]
operator|=
operator|new
name|ProgramParameter
argument_list|(
name|inputData
argument_list|,
name|outputLength
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
condition|)
block|{
name|parameterList
index|[
name|i
index|]
operator|=
operator|new
name|ProgramParameter
argument_list|(
name|inputData
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|output
condition|)
block|{
name|parameterList
index|[
name|i
index|]
operator|=
operator|new
name|ProgramParameter
argument_list|(
name|outputLength
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|parameterList
return|;
block|}
DECL|method|handlePGMOutput (Exchange exchange, ProgramCall pgmCall, ProgramParameter[] inputs)
specifier|private
name|void
name|handlePGMOutput
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ProgramCall
name|pgmCall
parameter_list|,
name|ProgramParameter
index|[]
name|inputs
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Object
name|bodyIN
init|=
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
index|[]
name|params
init|=
operator|(
name|String
index|[]
operator|)
name|bodyIN
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|1
decl_stmt|;
for|for
control|(
name|ProgramParameter
name|pgmParam
range|:
name|pgmCall
operator|.
name|getParameterList
argument_list|()
control|)
block|{
name|byte
index|[]
name|output
init|=
name|pgmParam
operator|.
name|getOutputData
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|params
index|[
name|i
operator|-
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|int
name|length
init|=
name|pgmParam
operator|.
name|getOutputDataLength
argument_list|()
decl_stmt|;
name|AS400Text
name|text
init|=
operator|new
name|AS400Text
argument_list|(
name|length
argument_list|)
decl_stmt|;
name|value
operator|=
operator|(
name|String
operator|)
name|text
operator|.
name|toObject
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
name|results
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|String
index|[]
name|bodyOUT
init|=
operator|new
name|String
index|[
name|results
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|bodyOUT
operator|=
name|results
operator|.
name|toArray
argument_list|(
name|bodyOUT
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|bodyOUT
argument_list|)
expr_stmt|;
block|}
DECL|method|getOutputMessages (ProgramCall pgmCall)
specifier|private
name|String
name|getOutputMessages
parameter_list|(
name|ProgramCall
name|pgmCall
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuilder
name|outputMsg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// Show messages.
name|AS400Message
index|[]
name|messageList
init|=
name|pgmCall
operator|.
name|getMessageList
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
name|messageList
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
comment|// Load additional message information.
name|messageList
index|[
name|i
index|]
operator|.
name|load
argument_list|()
expr_stmt|;
name|outputMsg
operator|.
name|append
argument_list|(
name|i
operator|+
literal|") "
argument_list|)
expr_stmt|;
name|outputMsg
operator|.
name|append
argument_list|(
name|messageList
index|[
name|i
index|]
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|outputMsg
operator|.
name|append
argument_list|(
literal|" - "
argument_list|)
expr_stmt|;
name|outputMsg
operator|.
name|append
argument_list|(
name|messageList
index|[
name|i
index|]
operator|.
name|getHelp
argument_list|()
argument_list|)
expr_stmt|;
name|outputMsg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|outputMsg
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


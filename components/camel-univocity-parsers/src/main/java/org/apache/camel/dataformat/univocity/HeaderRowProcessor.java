begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.univocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|univocity
package|;
end_package

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|common
operator|.
name|ParsingContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|common
operator|.
name|processor
operator|.
name|RowProcessor
import|;
end_import

begin_comment
comment|/**  * This class is used by the unmarshaller in order to retrieve the headers.  */
end_comment

begin_class
DECL|class|HeaderRowProcessor
specifier|final
class|class
name|HeaderRowProcessor
implements|implements
name|RowProcessor
block|{
DECL|field|headers
specifier|private
name|String
index|[]
name|headers
decl_stmt|;
comment|/**      * Called when the processing starts, it clears the headers      *      * @param context Parsing context      */
annotation|@
name|Override
DECL|method|processStarted (ParsingContext context)
specifier|public
name|void
name|processStarted
parameter_list|(
name|ParsingContext
name|context
parameter_list|)
block|{
name|headers
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Called when a row is processed, it retrieve the headers if necessary.      *      * @param row     Processed row      * @param context Parsing context      */
annotation|@
name|Override
DECL|method|rowProcessed (String[] row, ParsingContext context)
specifier|public
name|void
name|rowProcessed
parameter_list|(
name|String
index|[]
name|row
parameter_list|,
name|ParsingContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
name|context
operator|.
name|headers
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Called when the processing completes, it clears the headers.      *      * @param context Parsing context      */
annotation|@
name|Override
DECL|method|processEnded (ParsingContext context)
specifier|public
name|void
name|processEnded
parameter_list|(
name|ParsingContext
name|context
parameter_list|)
block|{
name|headers
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Gets the headers.      *      * @return the headers      */
DECL|method|getHeaders ()
specifier|public
name|String
index|[]
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
block|}
end_class

end_unit


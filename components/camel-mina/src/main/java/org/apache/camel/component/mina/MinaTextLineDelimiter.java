begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|textline
operator|.
name|LineDelimiter
import|;
end_import

begin_comment
comment|/**  * Possible text line delimiters to be used with the textline codec.  */
end_comment

begin_enum
DECL|enum|MinaTextLineDelimiter
specifier|public
enum|enum
name|MinaTextLineDelimiter
block|{
DECL|enumConstant|DEFAULT
name|DEFAULT
parameter_list|(
name|LineDelimiter
operator|.
name|DEFAULT
parameter_list|)
operator|,
DECL|enumConstant|AUTO
constructor|AUTO(LineDelimiter.AUTO
block|)
enum|,
DECL|enumConstant|UNIX
name|UNIX
argument_list|(
name|LineDelimiter
operator|.
name|UNIX
argument_list|)
operator|,
DECL|enumConstant|WINDOWS
name|WINDOWS
argument_list|(
name|LineDelimiter
operator|.
name|WINDOWS
argument_list|)
operator|,
DECL|enumConstant|MAC
name|MAC
argument_list|(
name|LineDelimiter
operator|.
name|MAC
argument_list|)
enum|;
end_enum

begin_decl_stmt
DECL|field|lineDelimiter
specifier|private
specifier|final
name|LineDelimiter
name|lineDelimiter
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|MinaTextLineDelimiter (LineDelimiter lineDelimiter)
name|MinaTextLineDelimiter
argument_list|(
name|LineDelimiter
name|lineDelimiter
argument_list|)
block|{
name|this
operator|.
name|lineDelimiter
operator|=
name|lineDelimiter
block|;     }
DECL|method|getLineDelimiter ()
specifier|public
name|LineDelimiter
name|getLineDelimiter
argument_list|()
block|{
return|return
name|lineDelimiter
return|;
block|}
end_expr_stmt

unit|}
end_unit


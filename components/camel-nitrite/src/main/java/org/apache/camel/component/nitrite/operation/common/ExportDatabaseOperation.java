begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nitrite.operation.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nitrite
operator|.
name|operation
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|component
operator|.
name|nitrite
operator|.
name|AbstractNitriteOperation
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
name|nitrite
operator|.
name|NitriteEndpoint
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
name|nitrite
operator|.
name|operation
operator|.
name|CommonOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dizitart
operator|.
name|no2
operator|.
name|tool
operator|.
name|ExportOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dizitart
operator|.
name|no2
operator|.
name|tool
operator|.
name|Exporter
import|;
end_import

begin_comment
comment|/**  * Export full database to JSON and stores result in body - see Nitrite docs for details about format  */
end_comment

begin_class
DECL|class|ExportDatabaseOperation
specifier|public
class|class
name|ExportDatabaseOperation
extends|extends
name|AbstractNitriteOperation
implements|implements
name|CommonOperation
block|{
DECL|field|options
specifier|private
name|ExportOptions
name|options
decl_stmt|;
DECL|method|ExportDatabaseOperation ()
specifier|public
name|ExportDatabaseOperation
parameter_list|()
block|{     }
DECL|method|ExportDatabaseOperation (ExportOptions options)
specifier|public
name|ExportDatabaseOperation
parameter_list|(
name|ExportOptions
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (Exchange exchange, NitriteEndpoint endpoint)
specifier|protected
name|void
name|execute
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|NitriteEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|stream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Exporter
name|exporter
init|=
name|Exporter
operator|.
name|of
argument_list|(
name|endpoint
operator|.
name|getNitriteDatabase
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
name|exporter
operator|.
name|withOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
name|exporter
operator|.
name|exportTo
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|stream
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


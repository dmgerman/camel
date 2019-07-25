begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|Expression
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
name|Predicate
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

begin_comment
comment|/**  * Some useful converters for Camel APIs such as to convert a {@link Predicate} or {@link Expression}  * to a {@link Processor}  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|CamelConverter
specifier|public
specifier|final
class|class
name|CamelConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|CamelConverter ()
specifier|private
name|CamelConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toProcessor (final Predicate predicate)
specifier|public
specifier|static
name|Processor
name|toProcessor
parameter_list|(
specifier|final
name|Predicate
name|predicate
parameter_list|)
block|{
return|return
name|exchange
lambda|->
block|{
name|boolean
name|answer
init|=
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
return|;
block|}
annotation|@
name|Converter
DECL|method|toProcessor (final Expression expression)
specifier|public
specifier|static
name|Processor
name|toProcessor
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
name|exchange
lambda|->
block|{
name|Object
name|answer
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
return|;
block|}
block|}
end_class

end_unit


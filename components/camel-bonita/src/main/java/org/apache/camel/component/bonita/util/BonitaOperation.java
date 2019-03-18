begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
operator|.
name|util
package|;
end_package

begin_enum
DECL|enum|BonitaOperation
specifier|public
enum|enum
name|BonitaOperation
block|{
DECL|enumConstant|startCase
name|startCase
argument_list|(
literal|"startCase"
argument_list|)
block|;
DECL|field|text
specifier|private
specifier|final
name|String
name|text
decl_stmt|;
DECL|method|BonitaOperation (final String text)
name|BonitaOperation
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
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
name|text
return|;
block|}
block|}
end_enum

end_unit


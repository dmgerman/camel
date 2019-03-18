begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pdf.text
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pdf
operator|.
name|text
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|PatternSplitStrategy
specifier|public
class|class
name|PatternSplitStrategy
implements|implements
name|SplitStrategy
block|{
DECL|field|splitPattern
specifier|private
specifier|final
name|String
name|splitPattern
decl_stmt|;
DECL|method|PatternSplitStrategy (String splitPattern)
specifier|public
name|PatternSplitStrategy
parameter_list|(
name|String
name|splitPattern
parameter_list|)
block|{
name|this
operator|.
name|splitPattern
operator|=
name|splitPattern
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|split (String text)
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|split
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|text
operator|.
name|split
argument_list|(
name|splitPattern
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit


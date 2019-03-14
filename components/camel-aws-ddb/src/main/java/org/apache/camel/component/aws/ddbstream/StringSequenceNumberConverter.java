begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddbstream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|ddbstream
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

begin_comment
comment|// Allow to ignore this type converter if the ddbstream JARs are not present on the classpath
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|,
name|ignoreOnLoadError
operator|=
literal|true
argument_list|)
DECL|class|StringSequenceNumberConverter
specifier|public
specifier|final
class|class
name|StringSequenceNumberConverter
block|{
DECL|method|StringSequenceNumberConverter ()
specifier|private
name|StringSequenceNumberConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toSequenceNumberProvider (String sequenceNumber)
specifier|public
specifier|static
name|SequenceNumberProvider
name|toSequenceNumberProvider
parameter_list|(
name|String
name|sequenceNumber
parameter_list|)
block|{
return|return
operator|new
name|StaticSequenceNumberProvider
argument_list|(
name|sequenceNumber
argument_list|)
return|;
block|}
block|}
end_class

end_unit


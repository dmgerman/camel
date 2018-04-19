begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
package|;
end_package

begin_interface
DECL|interface|Constants
interface|interface
name|Constants
block|{
DECL|field|SHA_1_AS2_ALGORITHM_NAME
specifier|static
specifier|final
name|String
name|SHA_1_AS2_ALGORITHM_NAME
init|=
literal|"sha1"
decl_stmt|;
DECL|field|SHA_1_JDK_ALGORITHM_NAME
specifier|static
specifier|final
name|String
name|SHA_1_JDK_ALGORITHM_NAME
init|=
literal|"SHA-1"
decl_stmt|;
DECL|field|MD5_AS2_ALGORITHM_NAME
specifier|static
specifier|final
name|String
name|MD5_AS2_ALGORITHM_NAME
init|=
literal|"md5"
decl_stmt|;
DECL|field|MD5_JDK_ALGORITHM_NAME
specifier|static
specifier|final
name|String
name|MD5_JDK_ALGORITHM_NAME
init|=
literal|"MD5"
decl_stmt|;
block|}
end_interface

begin_enum
DECL|enum|AS2MicAlgorithm
specifier|public
enum|enum
name|AS2MicAlgorithm
block|{
DECL|enumConstant|SHA_1
name|SHA_1
parameter_list|(
name|Constants
operator|.
name|SHA_1_JDK_ALGORITHM_NAME
parameter_list|,
name|Constants
operator|.
name|SHA_1_AS2_ALGORITHM_NAME
parameter_list|)
operator|,
DECL|enumConstant|MD5
constructor|MD5(Constants.MD5_JDK_ALGORITHM_NAME
operator|,
constructor|Constants.MD5_AS2_ALGORITHM_NAME
block|)
enum|;
end_enum

begin_decl_stmt
DECL|field|jdkAlgorithmName
specifier|private
name|String
name|jdkAlgorithmName
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|as2AlgorithmName
specifier|private
name|String
name|as2AlgorithmName
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|AS2MicAlgorithm (String jdkAlgorithmName, String as2AlgorithmName)
specifier|private
name|AS2MicAlgorithm
parameter_list|(
name|String
name|jdkAlgorithmName
parameter_list|,
name|String
name|as2AlgorithmName
parameter_list|)
block|{
name|this
operator|.
name|jdkAlgorithmName
operator|=
name|jdkAlgorithmName
expr_stmt|;
name|this
operator|.
name|as2AlgorithmName
operator|=
name|as2AlgorithmName
expr_stmt|;
block|}
end_constructor

begin_function
DECL|method|getJdkAlgorithmName ()
specifier|public
name|String
name|getJdkAlgorithmName
parameter_list|()
block|{
return|return
name|jdkAlgorithmName
return|;
block|}
end_function

begin_function
DECL|method|getAs2AlgorithmName ()
specifier|public
name|String
name|getAs2AlgorithmName
parameter_list|()
block|{
return|return
name|as2AlgorithmName
return|;
block|}
end_function

begin_function
DECL|method|getJdkAlgorithmName (String as2AlgorithmName)
specifier|public
specifier|static
name|String
name|getJdkAlgorithmName
parameter_list|(
name|String
name|as2AlgorithmName
parameter_list|)
block|{
switch|switch
condition|(
name|as2AlgorithmName
condition|)
block|{
case|case
name|Constants
operator|.
name|SHA_1_AS2_ALGORITHM_NAME
case|:
return|return
name|Constants
operator|.
name|SHA_1_JDK_ALGORITHM_NAME
return|;
case|case
name|Constants
operator|.
name|MD5_AS2_ALGORITHM_NAME
case|:
return|return
name|Constants
operator|.
name|MD5_JDK_ALGORITHM_NAME
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
end_function

begin_function
DECL|method|getAS2AlgorithmName (String jdkAlgorithmName)
specifier|public
specifier|static
name|String
name|getAS2AlgorithmName
parameter_list|(
name|String
name|jdkAlgorithmName
parameter_list|)
block|{
switch|switch
condition|(
name|jdkAlgorithmName
condition|)
block|{
case|case
name|Constants
operator|.
name|MD5_JDK_ALGORITHM_NAME
case|:
return|return
name|Constants
operator|.
name|MD5_AS2_ALGORITHM_NAME
return|;
case|case
name|Constants
operator|.
name|SHA_1_JDK_ALGORITHM_NAME
case|:
return|return
name|Constants
operator|.
name|SHA_1_AS2_ALGORITHM_NAME
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
end_function

unit|}
end_unit


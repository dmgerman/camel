begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spark
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
package|;
end_package

begin_class
DECL|class|SparkConstants
specifier|public
specifier|final
class|class
name|SparkConstants
block|{
DECL|field|SPARK_RDD_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|SPARK_RDD_HEADER
init|=
literal|"CAMEL_SPARK_RDD"
decl_stmt|;
DECL|field|SPARK_RDD_CALLBACK_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|SPARK_RDD_CALLBACK_HEADER
init|=
literal|"CAMEL_SPARK_RDD_CALLBACK"
decl_stmt|;
DECL|field|SPARK_DATAFRAME_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|SPARK_DATAFRAME_HEADER
init|=
literal|"CAMEL_SPARK_DATAFRAME"
decl_stmt|;
DECL|field|SPARK_DATAFRAME_CALLBACK_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|SPARK_DATAFRAME_CALLBACK_HEADER
init|=
literal|"CAMEL_SPARK_DATAFRAME_CALLBACK"
decl_stmt|;
DECL|method|SparkConstants ()
specifier|private
name|SparkConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit


begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"data"
argument_list|)
DECL|class|HBaseData
specifier|public
class|class
name|HBaseData
block|{
DECL|field|rows
specifier|private
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|rows
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|HBaseData ()
specifier|public
name|HBaseData
parameter_list|()
block|{     }
DECL|method|HBaseData (List<HBaseRow> rows)
specifier|public
name|HBaseData
parameter_list|(
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|rows
parameter_list|)
block|{
name|this
operator|.
name|rows
operator|=
name|rows
expr_stmt|;
block|}
DECL|method|getRows ()
specifier|public
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|getRows
parameter_list|()
block|{
return|return
name|rows
return|;
block|}
DECL|method|setRows (List<HBaseRow> rows)
specifier|public
name|void
name|setRows
parameter_list|(
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|rows
parameter_list|)
block|{
name|this
operator|.
name|rows
operator|=
name|rows
expr_stmt|;
block|}
block|}
end_class

end_unit


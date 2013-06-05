begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
package|;
end_package

begin_comment
comment|/**  * Abstract base DTO for Salesforce SOQL Query records.  *<p>  * Derived classes must follow the template below:  *</p>  *<pre>  * {@code  * public class QueryResultMySObject extends AbstractQueryRecordsBase {  *     @XStreamImplicit  *     private List<MySObject> records;  *  *     public List<MySObject> getRecords() {  *         return records;  *     }  *  *     public void setRecords(List<MySObject> records) {  *         this.records = records;  *     }  *  * }  * }  *</pre>  */
end_comment

begin_class
DECL|class|AbstractQueryRecordsBase
specifier|public
specifier|abstract
class|class
name|AbstractQueryRecordsBase
extends|extends
name|AbstractDTOBase
block|{
DECL|field|done
specifier|private
name|Boolean
name|done
decl_stmt|;
DECL|field|totalSize
specifier|private
name|int
name|totalSize
decl_stmt|;
DECL|field|nextRecordsUrl
specifier|private
name|String
name|nextRecordsUrl
decl_stmt|;
DECL|method|getDone ()
specifier|public
name|Boolean
name|getDone
parameter_list|()
block|{
return|return
name|done
return|;
block|}
DECL|method|setDone (Boolean done)
specifier|public
name|void
name|setDone
parameter_list|(
name|Boolean
name|done
parameter_list|)
block|{
name|this
operator|.
name|done
operator|=
name|done
expr_stmt|;
block|}
DECL|method|getTotalSize ()
specifier|public
name|int
name|getTotalSize
parameter_list|()
block|{
return|return
name|totalSize
return|;
block|}
DECL|method|setTotalSize (int totalSize)
specifier|public
name|void
name|setTotalSize
parameter_list|(
name|int
name|totalSize
parameter_list|)
block|{
name|this
operator|.
name|totalSize
operator|=
name|totalSize
expr_stmt|;
block|}
DECL|method|getNextRecordsUrl ()
specifier|public
name|String
name|getNextRecordsUrl
parameter_list|()
block|{
return|return
name|nextRecordsUrl
return|;
block|}
DECL|method|setNextRecordsUrl (String nextRecordsUrl)
specifier|public
name|void
name|setNextRecordsUrl
parameter_list|(
name|String
name|nextRecordsUrl
parameter_list|)
block|{
name|this
operator|.
name|nextRecordsUrl
operator|=
name|nextRecordsUrl
expr_stmt|;
block|}
block|}
end_class

end_unit


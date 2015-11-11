package com.adapter;

/* Copyright (C) 2006 The Android Open Source Project Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License. */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Slightly adopted ArrayAdapter. Basically a simple copy of ArrayAdapter with an extracted method filterObject() to
 * allow easy change of the filtering-behaviour.
 * <p/>
 * A concrete BaseAdapter that is backed by an array of arbitrary objects. By default this class expects that the
 * provided resource id references a single TextView. If you want to use a more complex layout, use the constructors
 * that also takes a field id. That field id should reference a TextView in the larger layout resource.
 * <p/>
 * <p/>
 * However the TextView is referenced, it will be filled with the toString() of each object in the array. You can add
 * lists or arrays of custom objects. Override the toString() method of your objects to determine what text will be
 * displayed for the item in the list.
 * <p/>
 * <p/>
 * To use something other than TextViews for the array display, for instance, ImageViews, or to have some of data
 * besides toString() results fill the views.
 *
 * @param <T> The class, this adapter should hold
 */
public abstract class FilterableAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> implements Filterable {
    /**
     * Contains the list of objects that represent the data of this ArrayAdapter. The content of this list is referred
     * to as "the array" in the documentation.
     */
    private List<T> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation performed on the array should be
     * synchronized on this lock. This lock is also used by the filter (see {@link #getFilter()} to make a synchronized
     * copy of the original array of data.
     */
    protected final Object LOCK = new Object();

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private ArrayList<T> mOriginalValues;
    private TextFilter mFilter;

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public FilterableAdapter(Context context) {
        init(context, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public FilterableAdapter(Context context, T[] objects) {
        init(context, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public FilterableAdapter(Context context, List<T> objects) {
        init(context, objects);
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        synchronized (LOCK) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }


    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        synchronized (LOCK) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        synchronized (LOCK) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        synchronized (LOCK) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        synchronized (LOCK) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (LOCK) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        synchronized (LOCK) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Control whether methods that change the list ({@link #add}, {@link #insert}, {@link #remove}, {@link #clear})
     * automatically call {@link #notifyDataSetChanged}. If set to false, caller must manually call
     * notifyDataSetChanged() to have the changes reflected in the attached view.
     * <p/>
     * The default is true, and calling notifyDataSetChanged() resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will automatically call {@link #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(Context context, List<T> objects) {
        mContext = context;
        mObjects = objects;
    }

    /**
     * Returns the context associated with this array adapter. The context is used to create views from the resource
     * passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    /**
     * Get item at position
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    public List<T> getFilterItems() {
        return mObjects;
    }
    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public TextFilter getFilter() {
        if (mFilter == null) {
            mFilter = new TextFilter();
        }
        return mFilter;
    }

    /**
     * Performs filtering on the provided object and returns true, if the object should be in the filtered collection,
     * or false if it shouldn't.
     *
     * @param myObject   The object to be inspected
     * @param constraint Constraint, that the object has to fulfil
     * @return true, if the object should be in the filteredResult, false otherwise
     */
    protected abstract boolean filterObject(T myObject, String constraint);
//    protected boolean filterObject(T myObject, String constraint) {
//        final String valueText = myObject.toString().toLowerCase();
//
//        // First match against the whole, non-splitted value
//        if (valueText.contains(constraint)) {
//            return true;
//        } else {
//            final String[] words = valueText.split(" ");
//
//            // Start at index 0, in case valueText starts with space(s)
//            for (String word : words) {
//                if (word.contains(constraint)) {
//                    return true;
//                }
//            }
//        }
//
//        // No match, so don't add to collection
//        return false;
//    }

    /**
     * <p>
     * An array filter constrains the content of the array adapter with a prefix. Each item that does not start with the
     * supplied prefix is removed from the list.
     * </p>
     */
    protected class TextFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (LOCK) {
                    mOriginalValues = new ArrayList<T>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<T> list;
                synchronized (LOCK) {
                    list = new ArrayList<T>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<T> values;
                synchronized (LOCK) {
                    values = new ArrayList<T>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    if (filterObject(value, prefixString)) {
                        newValues.add(value);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
//            if (results.count > 0) {
            notifyDataSetChanged();
//            } else {
//                notifyDataSetInvalidated();
//            }
        }
    }
}
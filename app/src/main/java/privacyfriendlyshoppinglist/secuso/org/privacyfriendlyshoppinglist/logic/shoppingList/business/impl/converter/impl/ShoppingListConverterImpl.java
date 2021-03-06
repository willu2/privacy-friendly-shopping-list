package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.impl.converter.impl;

import android.content.Context;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.comparators.PFAComparators;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.persistence.DB;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.DateUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.StringUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.impl.converter.ShoppingListConverter;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.persistence.entity.ShoppingListEntity;

import java.util.Date;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 11.06.16 creation date
 */
public class ShoppingListConverterImpl implements ShoppingListConverter
{
    private static final String SPACE = " ";

    private String language;
    private String dateLongPattern;
    private String datePattern;
    private String timePattern;
    private Context context;

    @Override
    public void setContext(Context context, DB db)
    {
        this.context = context;
        this.language = context.getResources().getString(R.string.language);
        this.dateLongPattern = context.getResources().getString(R.string.date_long_pattern);
        this.datePattern = context.getResources().getString(R.string.date_short_pattern);
        this.timePattern = context.getResources().getString(R.string.time_pattern);
    }

    @Override
    public void convertDtoToEntity(ListDto dto, ShoppingListEntity entity)
    {
        Long id = getIdAsLong(dto);
        entity.setId(id);
        entity.setListName(dto.getListName());
        entity.setSortAscending(dto.isSortAscending());
        entity.setSortCriteria(dto.getSortCriteria());
        entity.setStatisticsEnabled(dto.isStatisticEnabled());

        String fullDate = dto.getDeadlineDate() + SPACE + dto.getDeadlineTime();
        if ( !SPACE.equals(fullDate) )
        {
            Date deadline = DateUtils.getDateFromString(fullDate, dateLongPattern, language).toDate();
            entity.setDeadline(deadline);
            setReminder(dto, entity);
        }
        else
        {
            entity.setDeadline(null);
            entity.setReminderCount(null);
            entity.setReminderUnit(null);
        }
        entity.setIcon(dto.getIcon());
        entity.setNotes(dto.getNotes());
        entity.setPriority(dto.getPriority());
    }

    private void setReminder(ListDto dto, ShoppingListEntity entity)
    {
        if ( dto.getReminderCount() != null )
        {
            if ( dto.isReminderEnabled() )
            {
                if ( !StringUtils.isEmpty(dto.getReminderCount()) )
                {
                    entity.setReminderCount(Integer.valueOf(dto.getReminderCount()));
                }
                else
                {
                    entity.setReminderCount(0);
                }
            }
            else
            {
                entity.setReminderCount(null);
            }
            entity.setReminderUnit(Integer.valueOf(dto.getReminderUnit()));
        }
    }

    @Override
    public void convertEntityToDto(ShoppingListEntity entity, ListDto dto)
    {
        dto.setId(entity.getId().toString());
        dto.setListName(entity.getListName());
        dto.setStatisticEnabled(entity.getStatisticsEnabled());

        if ( entity.getSortCriteria() != null )
        {
            dto.setSortCriteria(entity.getSortCriteria());
            dto.setSortAscending(entity.getSortAscending());
        }
        else // default sort config
        {
            dto.setSortCriteria(PFAComparators.SORT_BY_NAME);
            dto.setSortAscending(true);
        }

        if ( entity.getDeadline() != null )
        {
            String deadlineDateAsString = DateUtils.getDateAsString(entity.getDeadline().getTime(), datePattern, language);
            String deadlineTimeAsString = DateUtils.getDateAsString(entity.getDeadline().getTime(), timePattern, language);
            dto.setDeadlineDate(deadlineDateAsString);
            dto.setDeadlineTime(deadlineTimeAsString);
        }
        else
        {
            dto.setDeadlineDate(StringUtils.EMPTY);
            dto.setDeadlineTime(StringUtils.EMPTY);
        }

        if ( entity.getReminderCount() != null )
        {
            dto.setReminderCount(String.valueOf(entity.getReminderCount()));
            dto.setReminderUnit(String.valueOf(entity.getReminderUnit()));
        }

        dto.setIcon(entity.getIcon());
        dto.setNotes(entity.getNotes());
        dto.setPriority(entity.getPriority());
    }

    private Long getIdAsLong(ListDto dto)
    {
        String stringId = dto.getId();
        return stringId == null ? null : Long.valueOf(stringId);
    }
}
